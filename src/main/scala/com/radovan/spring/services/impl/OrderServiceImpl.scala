package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{OrderDto, OrderItemDto}
import com.radovan.spring.entity.OrderItemEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.{OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.services.{CartItemService, CartService, CustomerService, OrderService, ShippingAddressService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.{Instant, ZoneId}
import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

@Service
class OrderServiceImpl extends OrderService {

  private var orderRepository: OrderRepository = _
  private var tempConverter: TempConverter = _
  private var orderItemRepository: OrderItemRepository = _
  private var orderAddressRepository: OrderAddressRepository = _
  private var cartItemService: CartItemService = _
  private var cartService: CartService = _
  private var customerService: CustomerService = _
  private var shippingAddressService: ShippingAddressService = _
  private val zoneId = ZoneId.of("UTC")


  @Autowired
  private def injectAll(orderRepository: OrderRepository, tempConverter: TempConverter, orderItemRepository: OrderItemRepository,
                        orderAddressRepository: OrderAddressRepository, cartItemService: CartItemService, cartService: CartService,
                        customerService: CustomerService, shippingAddressService: ShippingAddressService): Unit = {

    this.orderRepository = orderRepository
    this.tempConverter = tempConverter
    this.orderItemRepository = orderItemRepository
    this.orderAddressRepository = orderAddressRepository
    this.cartItemService = cartItemService
    this.cartService = cartService
    this.customerService = customerService
    this.shippingAddressService = shippingAddressService
  }

  @Transactional(readOnly = true)
  override def listAll: Array[OrderDto] = {
    val allOrders = orderRepository.findAll().asScala
    allOrders.collect {
      case orderEntity => tempConverter.orderEntityToDto(orderEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getOrderById(orderId: Integer): OrderDto = {
    val orderEntity = orderRepository.findById(orderId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The order has not been found")))
    tempConverter.orderEntityToDto(orderEntity)
  }

  @Transactional
  override def deleteOrder(orderId: Integer): Unit = {
    getOrderById(orderId)
    orderRepository.deleteById(orderId)
    orderRepository.flush()
  }

  @Transactional(readOnly = true)
  override def listAllByCartId(cartId: Integer): Array[OrderDto] = {
    cartService.getCartById(cartId)
    val allOrders = listAll
    allOrders.collect {
      case order if order.getCartId == cartId => order
    }
  }

  @Transactional
  override def addOrder: OrderDto = {
    var returnValue = new OrderDto
    val customer = customerService.getCurrentCustomer
    val cart = cartService.getCartById(customer.getCartId)
    cartService.validateCart(cart.getCartId)
    returnValue.setCartId(cart.getCartId)
    returnValue.setOrderPrice(cart.getCartPrice)
    val shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId)
    val orderAddress = tempConverter.shippingAddressToOrderAddress(shippingAddress)
    val storedAddress = orderAddressRepository.save(tempConverter.orderAddressDtoToEntity(orderAddress))
    val orderEntity = tempConverter.orderDtoToEntity(returnValue)
    orderEntity.setAddress(storedAddress)
    val currentTime = Instant.now().atZone(zoneId)
    orderEntity.setCreateTime(Timestamp.valueOf(currentTime.toLocalDateTime))
    var storedOrder = orderRepository.save(orderEntity)

    val orderedItems = new ArrayBuffer[OrderItemDto]()
    val cartItems = cartItemService.listAllByCartId(cart.getCartId)
    cartItems.foreach(cartItem => {
      val orderItem = tempConverter.cartItemToOrderItem(cartItem)
      orderedItems += orderItem
    })

    val allOrderedItems = new ArrayBuffer[OrderItemEntity]()
    orderedItems.foreach(orderItem => {
      orderItem.setOrderId(storedOrder.getOrderId)
      val storedItem = orderItemRepository.save(tempConverter.orderItemDtoToEntity(orderItem))
      allOrderedItems += storedItem
    })

    storedOrder.getOrderedItems.clear()
    storedOrder.getOrderedItems.addAll(allOrderedItems.asJava)
    storedOrder = orderRepository.saveAndFlush(storedOrder)
    returnValue = tempConverter.orderEntityToDto(storedOrder)
    cartItemService.eraseAllCartItems(cart.getCartId)
    cartService.refreshCartState(cart.getCartId)
    returnValue
  }
}
