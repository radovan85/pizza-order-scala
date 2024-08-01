package com.radovan.spring.converter

import com.radovan.spring.dto.{CartDto, CartItemDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, PizzaDto, PizzaSizeDto, RoleDto, ShippingAddressDto, UserDto}
import com.radovan.spring.entity.{CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, PizzaEntity, PizzaSizeEntity, RoleEntity, ShippingAddressEntity, UserEntity}
import com.radovan.spring.repositories.{CartItemRepository, CartRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, PizzaRepository, PizzaSizeRepository, RoleRepository, ShippingAddressRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.text.DecimalFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

@Component
class TempConverterImpl extends TempConverter {

  private var mapper: ModelMapper = _
  private var customerRepository: CustomerRepository = _
  private var pizzaSizeRepository: PizzaSizeRepository = _
  private var cartRepository: CartRepository = _
  private var shippingAddressRepository: ShippingAddressRepository = _
  private var userRepository: UserRepository = _
  private var roleRepository: RoleRepository = _
  private var pizzaRepository: PizzaRepository = _
  private var orderItemRepository: OrderItemRepository = _
  private var orderAddressRepository: OrderAddressRepository = _
  private var orderRepository: OrderRepository = _
  private var cartItemRepository:CartItemRepository = _
  private val decfor = new DecimalFormat("0.00")
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val zoneId = ZoneId.of("UTC")

  override def cartEntityToDto(cart: CartEntity): CartDto = {
    val returnValue = mapper.map(cart, classOf[CartDto])
    val customerOpt = Option(cart.getCustomer)
    customerOpt match {
      case Some(customer) => returnValue.setCustomerId(customer.getCustomerId)
      case None =>
    }

    val cartItemsOpt = Option(cart.getCartItems.asScala)
    val cartItemsIds = new ArrayBuffer[Integer]()
    if (cartItemsOpt.isDefined) {
      cartItemsOpt.get.foreach(item => cartItemsIds += item.getCartItemId)
    }

    returnValue.setCartItemsIds(cartItemsIds.toArray)
    returnValue.setCartPrice(decfor.format(returnValue.getCartPrice).toFloat)
    returnValue
  }

  override def cartDtoToEntity(cart: CartDto): CartEntity = {
    val returnValue = mapper.map(cart, classOf[CartEntity])
    val customerIdOpt = Option(cart.getCustomerId)
    customerIdOpt match {
      case Some(customerId) =>
        val customer = customerRepository.findById(customerId).orElse(null)
        if (customer != null) {
          returnValue.setCustomer(customer)
        }
      case None =>
    }

    val cartItems = new ArrayBuffer[CartItemEntity]()
    val cartItemsIdsOpt = Option(cart.getCartItemsIds)
    cartItemsIdsOpt match {
      case Some(cartItemsIds) =>
        cartItemsIds.foreach(itemId => {
          val cartItemEntity = cartItemRepository.findById(itemId).orElse(null)
          if(cartItemEntity != null){
            cartItems += cartItemEntity
          }
        })
      case None =>
    }

    returnValue.setCartPrice(decfor.format(returnValue.getCartPrice).toFloat)
    returnValue.setCartItems(cartItems.asJava)
    returnValue
  }

  override def cartItemEntityToDto(cartItem: CartItemEntity): CartItemDto = {
    val returnValue = mapper.map(cartItem, classOf[CartItemDto])
    val pizzaSizeOpt = Option(cartItem.getPizzaSize)
    if (pizzaSizeOpt.isDefined) {
      returnValue.setPizzaSizeId(pizzaSizeOpt.get.getPizzaSizeId)
      val price = pizzaSizeOpt.get.getPrice * returnValue.getQuantity
      returnValue.setPrice(price)
    }

    val cartOpt = Option(cartItem.getCart)
    cartOpt match {
      case Some(cart) => returnValue.setCartId(cart.getCartId)
      case None =>
    }

    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  override def cartItemDtoToEntity(cartItem: CartItemDto): CartItemEntity = {
    val returnValue = mapper.map(cartItem, classOf[CartItemEntity])
    val pizzaSizeIdOpt = Option(cartItem.getPizzaSizeId)
    pizzaSizeIdOpt match {
      case Some(pizzaSizeId) =>
        val pizzaSizeEntity = pizzaSizeRepository.findById(pizzaSizeId).orElse(null)
        if (pizzaSizeEntity != null) {
          returnValue.setPizzaSize(pizzaSizeEntity)
          val price = pizzaSizeEntity.getPrice * returnValue.getQuantity
          returnValue.setPrice(price)
        }
      case None =>
    }

    val cartIdOpt = Option(cartItem.getCartId)
    cartIdOpt match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if (cartEntity != null) {
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  override def customerEntityToDto(customer: CustomerEntity): CustomerDto = {
    val returnValue = mapper.map(customer, classOf[CustomerDto])
    val addressOpt = Option(customer.getShippingAddress)
    if (addressOpt.isDefined) {
      returnValue.setShippingAddressId(addressOpt.get.getShippingAddressId)
    }

    val userOpt = Option(customer.getUser)
    if (userOpt.isDefined) {
      returnValue.setUserId(userOpt.get.getId)
    }

    val cartOpt = Option(customer.getCart)
    if (cartOpt.isDefined) {
      returnValue.setCartId(cartOpt.get.getCartId)
    }

    returnValue
  }

  override def customerDtoToEntity(customer: CustomerDto): CustomerEntity = {
    val returnValue = mapper.map(customer, classOf[CustomerEntity])
    val addressIdOpt = Option(customer.getShippingAddressId)
    addressIdOpt match {
      case Some(addressId) =>
        val addressEntity = shippingAddressRepository.findById(addressId).orElse(null)
        if (addressEntity != null) {
          returnValue.setShippingAddress(addressEntity)
        }
      case None =>
    }

    val userIdOpt = Option(customer.getUserId)
    userIdOpt match {
      case Some(userId) =>
        val userEntity = userRepository.findById(userId).orElse(null)
        if (userEntity != null) {
          returnValue.setUser(userEntity)
        }
      case None =>
    }

    val cartIdOpt = Option(customer.getCartId)
    cartIdOpt match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if (cartEntity != null) {
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    returnValue
  }

  override def shippingAddressEntityToDto(address: ShippingAddressEntity): ShippingAddressDto = {
    val returnValue = mapper.map(address, classOf[ShippingAddressDto])
    val customerOpt = Option(address.getCustomer)
    if (customerOpt.isDefined) {
      returnValue.setCustomerId(customerOpt.get.getCustomerId)
    }

    returnValue
  }

  override def shippingAddressDtoToEntity(address: ShippingAddressDto): ShippingAddressEntity = {
    val returnValue = mapper.map(address, classOf[ShippingAddressEntity])
    val customerIdOpt = Option(address.getCustomerId)
    customerIdOpt match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if (customerEntity != null) {
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  override def roleEntityToDto(role: RoleEntity): RoleDto = {
    val returnValue = mapper.map(role, classOf[RoleDto])
    val usersOpt = Option(role.getUsers.asScala)
    val usersIds = new ArrayBuffer[Integer]()
    if (usersOpt.isDefined) {
      usersOpt.get.foreach(userEntity => usersIds += userEntity.getId)
    }

    returnValue.setUsersIds(usersIds.toArray)
    returnValue
  }

  override def roleDtoToEntity(role: RoleDto): RoleEntity = {
    val returnValue = mapper.map(role, classOf[RoleEntity])
    val usersIdsOpt = Option(role.getUsersIds)
    val users = new ArrayBuffer[UserEntity]()
    usersIdsOpt match {
      case Some(usersIds) =>
        usersIds.foreach(userId => {
          val userEntity = userRepository.findById(userId).orElse(null)
          if (userEntity != null) {
            users += userEntity
          }
        })
      case None =>
    }

    returnValue.setUsers(users.asJava)
    returnValue
  }

  override def userEntityToDto(user: UserEntity): UserDto = {
    val returnValue = mapper.map(user, classOf[UserDto])
    val rolesOpt = Option(user.getRoles.asScala)
    val rolesIds = new ArrayBuffer[Integer]()
    if (rolesOpt.isDefined) {
      rolesOpt.get.foreach(role => rolesIds += role.getId)
    }

    val enabledOpt = Option(user.getEnabled)
    enabledOpt match {
      case Some(enabled) => returnValue.setEnabled(enabled.asInstanceOf[Short])
      case None =>
    }

    returnValue.setRolesIds(rolesIds.toArray)
    returnValue
  }

  override def userDtoToEntity(user: UserDto): UserEntity = {
    val returnValue = mapper.map(user, classOf[UserEntity])
    val rolesIdsOpt = Option(user.getRolesIds)
    val roles = new ArrayBuffer[RoleEntity]()
    rolesIdsOpt match {
      case Some(rolesIds) =>
        rolesIds.foreach(roleId => {
          val roleEntity = roleRepository.findById(roleId).orElse(null)
          if (roleEntity != null) {
            roles += roleEntity
          }
        })
      case None =>
    }

    val enabledOpt = Option(user.getEnabled)
    enabledOpt match {
      case Some(enabled) => returnValue.setEnabled(enabled.asInstanceOf[Byte])
      case None =>
    }

    returnValue.setRoles(roles.asJava)
    returnValue
  }

  override def pizzaEntityToDto(pizza: PizzaEntity): PizzaDto = {
    val returnValue = mapper.map(pizza, classOf[PizzaDto])
    val pizzaSizesOpt = Option(pizza.getPizzaSizes.asScala)
    val pizzaSizesIds = new ArrayBuffer[Integer]()
    if (pizzaSizesOpt.isDefined) {
      pizzaSizesOpt.get.foreach(pizzaSize => pizzaSizesIds += pizzaSize.getPizzaSizeId)
    }

    returnValue.setPizzaSizesIds(pizzaSizesIds.toArray)
    returnValue
  }


  override def pizzaDtoToEntity(pizza: PizzaDto): PizzaEntity = {
    val returnValue = mapper.map(pizza, classOf[PizzaEntity])
    val pizzaSizesIdsOpt = Option(pizza.getPizzaSizesIds)
    val pizzaSizes = new ArrayBuffer[PizzaSizeEntity]()

    pizzaSizesIdsOpt match {
      case Some(pizzaSizesIds) =>
        pizzaSizesIds.foreach(pizzaSizeId => {
          val pizzaSizeEntity = pizzaSizeRepository.findById(pizzaSizeId).orElse(null)
          if (pizzaSizeEntity != null) {
            pizzaSizes += pizzaSizeEntity
          }
        })

      case None =>
    }

    returnValue.setPizzaSizes(pizzaSizes.asJava)
    returnValue
  }


  override def pizzaSizeEntityToDto(pizzaSize: PizzaSizeEntity): PizzaSizeDto = {
    val returnValue = mapper.map(pizzaSize, classOf[PizzaSizeDto])
    val pizzaOpt = Option(pizzaSize.getPizza)
    pizzaOpt match {
      case Some(pizza) => returnValue.setPizzaId(pizza.getPizzaId)
      case None =>
    }

    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  override def pizzaSizeDtoToEntity(pizzaSize: PizzaSizeDto): PizzaSizeEntity = {
    val returnValue = mapper.map(pizzaSize, classOf[PizzaSizeEntity])
    val pizzaIdOpt = Option(pizzaSize.getPizzaId)
    pizzaIdOpt match {
      case Some(pizzaId) =>
        val pizzaEntity = pizzaRepository.findById(pizzaId).orElse(null)
        if (pizzaEntity != null) {
          returnValue.setPizza(pizzaEntity)
        }
      case None =>
    }

    returnValue.setPrice(decfor.format(returnValue.getPrice).toFloat)
    returnValue
  }

  override def orderEntityToDto(order: OrderEntity): OrderDto = {
    val returnValue = mapper.map(order, classOf[OrderDto])
    val cartOpt = Option(order.getCart)
    if (cartOpt.isDefined) {
      returnValue.setCartId(cartOpt.get.getCartId)
    }

    val createTimeOpt = Option(order.getCreateTime)
    if (createTimeOpt.isDefined) {
      val createTime = createTimeOpt.get.toLocalDateTime.atZone(zoneId)
      val createTimeStr = createTime.format(formatter)
      returnValue.setCreateTime(createTimeStr)
    }

    val orderedItemsOpt = Option(order.getOrderedItems.asScala)
    val orderedItemsIds = new ArrayBuffer[Integer]()
    if (orderedItemsOpt.isDefined) {
      orderedItemsOpt.get.foreach(item => orderedItemsIds += item.getOrderItemId)
    }

    returnValue.setOrderedItemsIds(orderedItemsIds.toArray)

    val addressOpt = Option(order.getAddress)
    if (addressOpt.isDefined) {
      returnValue.setAddressId(addressOpt.get.getOrderAddressId)
    }

    returnValue
  }

  override def orderDtoToEntity(order: OrderDto): OrderEntity = {
    val returnValue = mapper.map(order, classOf[OrderEntity])
    val cartIdOpt = Option(order.getCartId)
    cartIdOpt match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if (cartEntity != null) {
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    val orderedItemsIdsOpt = Option(order.getOrderedItemsIds)
    val orderedItems = new ArrayBuffer[OrderItemEntity]()
    orderedItemsIdsOpt match {
      case Some(orderedItemsIds) =>
        orderedItemsIds.foreach(itemId => {
          val orderedItemEntity = orderItemRepository.findById(itemId).orElse(null)
          if (orderedItemEntity != null) {
            orderedItems += orderedItemEntity
          }
        })
      case None =>
    }

    returnValue.setOrderPrice(decfor.format(returnValue.getOrderPrice).toFloat)
    returnValue.setOrderedItems(orderedItems.asJava)

    val addressIdOpt = Option(order.getAddressId)
    addressIdOpt match {
      case Some(addressId) =>
        val addressEntity = orderAddressRepository.findById(addressId).orElse(null)
        if (addressEntity != null) {
          returnValue.setAddress(addressEntity)
        }
      case None =>
    }

    returnValue.setOrderPrice(decfor.format(returnValue.getOrderPrice).toFloat)
    returnValue
  }

  override def orderItemEntityToDto(orderItem: OrderItemEntity): OrderItemDto = {
    val returnValue = mapper.map(orderItem, classOf[OrderItemDto])
    val orderOpt = Option(orderItem.getOrder)
    if (orderOpt.isDefined) {
      returnValue.setOrderId(orderOpt.get.getOrderId)
    }

    returnValue
  }

  override def orderItemDtoToEntity(orderItem: OrderItemDto): OrderItemEntity = {
    val returnValue = mapper.map(orderItem, classOf[OrderItemEntity])
    val orderIdOpt = Option(orderItem.getOrderId)
    orderIdOpt match {
      case Some(orderId) =>
        val orderEntity = orderRepository.findById(orderId).orElse(null)
        if (orderEntity != null) {
          returnValue.setOrder(orderEntity)
        }
      case None =>
    }

    returnValue
  }

  override def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    val orderOpt = Option(address.getOrder)
    orderOpt match {
      case Some(orderEntity) => returnValue.setOrderId(orderEntity.getOrderId)
      case None =>
    }

    returnValue
  }

  override def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderIdOpt = Option(address.getOrderId)
    orderIdOpt match {
      case Some(orderId) =>
        val orderEntity = orderRepository.findById(orderId).orElse(null)
        if (orderEntity != null) {
          returnValue.setOrder(orderEntity)
        }
      case None =>
    }

    returnValue
  }

  override def shippingAddressToOrderAddress(address: ShippingAddressDto): OrderAddressDto = {
    mapper.map(address, classOf[OrderAddressDto])
  }

  override def cartItemToOrderItem(cartItem: CartItemDto): OrderItemDto = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemDto])
    val pizzaSizeIdOpt = Option(cartItem.getPizzaSizeId)
    pizzaSizeIdOpt match {
      case Some(pizzaSizeId) =>
        val pizzaSizeEntity = pizzaSizeRepository.findById(pizzaSizeId).orElse(null)
        returnValue.setPizzaSize(pizzaSizeEntity.getName)
        returnValue.setPizzaPrice(pizzaSizeEntity.getPrice)
        returnValue.setPizza(pizzaSizeEntity.getPizza.getName)
      case None =>
    }

    returnValue
  }

  @Autowired
  private def injectAll(mapper: ModelMapper, customerRepository: CustomerRepository, pizzaSizeRepository: PizzaSizeRepository,
                        cartRepository: CartRepository, shippingAddressRepository: ShippingAddressRepository, userRepository: UserRepository,
                        roleRepository: RoleRepository, pizzaRepository: PizzaRepository, orderItemRepository: OrderItemRepository,
                        orderAddressRepository: OrderAddressRepository, orderRepository: OrderRepository,cartItemRepository: CartItemRepository): Unit = {
    this.mapper = mapper
    this.customerRepository = customerRepository
    this.pizzaSizeRepository = pizzaSizeRepository
    this.cartRepository = cartRepository
    this.shippingAddressRepository = shippingAddressRepository
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.pizzaRepository = pizzaRepository
    this.orderItemRepository = orderItemRepository
    this.orderAddressRepository = orderAddressRepository
    this.orderRepository = orderRepository
    this.cartItemRepository = cartItemRepository
  }


}
