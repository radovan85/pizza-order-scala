package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{CartDto, CustomerDto}
import com.radovan.spring.entity.{RoleEntity, UserEntity}
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repositories.{CartRepository, CustomerRepository, RoleRepository, ShippingAddressRepository, UserRepository}
import com.radovan.spring.services.{CustomerService, OrderService, UserService}
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._
import scala.collection.mutable.ArrayBuffer

@Service
class CustomerServiceImpl extends CustomerService {

  private var customerRepository:CustomerRepository = _
  private var tempConverter:TempConverter = _
  private var cartRepository:CartRepository = _
  private var shippingAddressRepository:ShippingAddressRepository = _
  private var userRepository:UserRepository = _
  private var roleRepository:RoleRepository = _
  private var passwordEncoder:BCryptPasswordEncoder = _
  private var userService:UserService = _
  private var orderService:OrderService = _

  @Autowired
  def injectAll(customerRepository: CustomerRepository,tempConverter: TempConverter,cartRepository: CartRepository,
                shippingAddressRepository: ShippingAddressRepository,userRepository: UserRepository,roleRepository: RoleRepository,
                passwordEncoder: BCryptPasswordEncoder,userService: UserService,orderService: OrderService):Unit = {

    this.customerRepository = customerRepository
    this.tempConverter = tempConverter
    this.cartRepository = cartRepository
    this.shippingAddressRepository = shippingAddressRepository
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.passwordEncoder = passwordEncoder
    this.userService = userService
    this.orderService = orderService
  }

  @Transactional
  override def storeCustomer(form: RegistrationForm): CustomerDto = {
    val user = form.getUser
    val userOption = userRepository.findByEmail(user.getEmail)
    userOption match {
      case Some(_) => throw new ExistingInstanceException(new Error("This email already exists!"))
      case None =>
    }

    val roleEntity = roleRepository.findByRole("ROLE_USER").getOrElse(roleRepository.save(new RoleEntity("ROLE_USER")))
    user.setPassword(passwordEncoder.encode(user.getPassword))
    user.setEnabled(1.asInstanceOf[Short])
    val roles = new ArrayBuffer[RoleEntity]()
    roles += roleEntity
    val userEntity = tempConverter.userDtoToEntity(user)
    userEntity.setRoles(roles.asJava)
    val storedUser = userRepository.save(userEntity)
    val users = new ArrayBuffer[UserEntity]()
    val usersOption = Option(roleEntity.getUsers.asScala)
    usersOption match {
      case Some(value) =>
        value.foreach(tempUser => users += tempUser)
      case None =>
    }

    users += storedUser
    roleEntity.setUsers(users.asJava)
    roleRepository.saveAndFlush(roleEntity)

    val storedShippingAddress = shippingAddressRepository.save(tempConverter.shippingAddressDtoToEntity(form.getShippingAddress))

    val cart = new CartDto
    cart.setCartPrice(0f)
    val storedCart = cartRepository.save(tempConverter.cartDtoToEntity(cart))

    val customer = form.getCustomer
    customer.setUserId(storedUser.getId)
    customer.setShippingAddressId(storedShippingAddress.getShippingAddressId)
    customer.setCartId(storedCart.getCartId)
    val storedCustomer = customerRepository.save(tempConverter.customerDtoToEntity(customer))

    storedShippingAddress.setCustomer(storedCustomer)
    shippingAddressRepository.saveAndFlush(storedShippingAddress)

    storedCart.setCustomer(storedCustomer)
    cartRepository.saveAndFlush(storedCart)

    tempConverter.customerEntityToDto(storedCustomer)
  }

  @Transactional(readOnly = true)
  override def getAllCustomers: Array[CustomerDto] = {
    val allCustomers = customerRepository.findAll().asScala
    allCustomers.collect{
      case customerEntity => tempConverter.customerEntityToDto(customerEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getCustomerById(customerId: Integer):CustomerDto = {
    val customerEntity = customerRepository.findById(customerId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The customer has not been found!")))
    tempConverter.customerEntityToDto(customerEntity)
  }

  @Transactional(readOnly = true)
  override def getCustomerByUserId(userId: Integer): CustomerDto = {
    val customerOption = customerRepository.findByUserId(userId)
    customerOption match {
      case Some(customerEntity) => tempConverter.customerEntityToDto(customerEntity)
      case None => throw new InstanceUndefinedException(new Error("The customer has not been found!"))
    }
  }

  @Transactional
  override def addCustomer(customer: CustomerDto): CustomerDto = {
    val storedCustomer = customerRepository.save(tempConverter.customerDtoToEntity(customer))
    tempConverter.customerEntityToDto(storedCustomer)
  }

  @Transactional
  override def deleteCustomer(customerId: Integer): Unit = {
    val customer = getCustomerById(customerId)
    val allOrders = orderService.listAllByCartId(customer.getCartId)
    allOrders.foreach(order => {
      orderService.deleteOrder(order.getOrderId)
    })

    customerRepository.deleteById(customerId)
    customerRepository.flush()
  }

  @Transactional(readOnly = true)
  override def getCurrentCustomer: CustomerDto = {
    val authUser = userService.getCurrentUser
    getCustomerByUserId(authUser.getId)
  }
}
