package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartDto
import com.radovan.spring.exceptions.{InstanceUndefinedException, InvalidCartException}
import com.radovan.spring.repositories.CartRepository
import com.radovan.spring.services.{CartItemService, CartService, CustomerService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.jdk.CollectionConverters._

@Service
class CartServiceImpl extends CartService{

  private var cartRepository:CartRepository = _
  private var tempConverter:TempConverter = _
  private var customerService:CustomerService = _
  private var cartItemService:CartItemService = _

  @Autowired
  private def injectAll(cartRepository: CartRepository,tempConverter: TempConverter
                       ,cartItemService: CartItemService,customerService: CustomerService):Unit = {
    this.cartRepository = cartRepository
    this.tempConverter = tempConverter
    this.cartItemService = cartItemService
    this.customerService = customerService
  }

  @Transactional
  override def refreshCartState(cartId: Integer): Unit = {
    val cart = getCartById(cartId)
    val allCartItems = cartItemService.listAllByCartId(cartId)
    val cartState = allCartItems.map(_.getPrice).sum
    cart.setCartPrice(cartState)
    cartRepository.saveAndFlush(tempConverter.cartDtoToEntity(cart))
  }

  @Transactional
  override def refreshAllCartStates(): Unit = {
    val allCarts = cartRepository.findAll().asScala
    allCarts.foreach(cartEntity => refreshCartState(cartEntity.getCartId))
  }

  @Transactional(readOnly = true)
  override def validateCart(cartId: Integer): CartDto = {
    val cart = getCartById(cartId)
    if(cart.getCartItemsIds.length == 0) {
      throw new InvalidCartException(new Error("The cart is empty!"))
    }

    cart
  }

  @Transactional(readOnly = true)
  override def getMyCart: CartDto = {
    val customer = customerService.getCurrentCustomer
    getCartById(customer.getCartId)
  }

  @Transactional(readOnly = true)
  override def getCartById(cartId: Integer): CartDto = {
    val cartEntity = cartRepository.findById(cartId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The cart has not been found!")))
    tempConverter.cartEntityToDto(cartEntity)
  }
}
