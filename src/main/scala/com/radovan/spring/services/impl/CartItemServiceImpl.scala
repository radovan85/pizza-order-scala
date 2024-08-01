package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.CartItemRepository
import com.radovan.spring.services.{CartItemService, CartService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class CartItemServiceImpl extends CartItemService {

  private var itemRepository:CartItemRepository = _
  private var tempConverter:TempConverter = _
  private var cartService:CartService = _


  @Autowired
  private def injectAll(itemRepository: CartItemRepository,tempConverter: TempConverter,
                        cartService: CartService):Unit = {
    this.itemRepository = itemRepository
    this.tempConverter = tempConverter
    this.cartService = cartService
  }

  @Transactional
  override def addCartItem(cartItem: CartItemDto): CartItemDto = {
    val storedItem = itemRepository.save(tempConverter.cartItemDtoToEntity(cartItem))
    tempConverter.cartItemEntityToDto(storedItem)
  }

  @Transactional
  override def removeCartItem(itemId: Integer): Unit = {
    val cartItem = getItemById(itemId)
    itemRepository.deleteById(itemId)
    itemRepository.flush()
    cartService.refreshCartState(cartItem.getCartId)
  }

  @Transactional
  override def eraseAllCartItems(cartId: Integer): Unit = {
    val allItems = listAllByCartId(cartId)
    allItems.foreach(item => {
      removeCartItem(item.getCartItemId)
    })
    cartService.refreshCartState(cartId)
  }

  @Transactional(readOnly = true)
  override def listAllByCartId(cartId: Integer): Array[CartItemDto] = {
    val allItems = itemRepository.findAll().asScala
    allItems.collect {
      case itemEntity if itemEntity.getCart.getCartId == cartId => tempConverter.cartItemEntityToDto(itemEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByPizzaSizeId(pizzaSizeId: Integer): Array[CartItemDto] = {
    val allItems = itemRepository.findAll().asScala
    allItems.collect {
      case itemEntity if itemEntity.getPizzaSize.getPizzaSizeId == pizzaSizeId => tempConverter.cartItemEntityToDto(itemEntity)
    }.toArray
  }

  @Transactional
  override def eraseAllByPizzaSizeId(pizzaSizeId: Integer): Unit = {
    val allItems = listAllByPizzaSizeId(pizzaSizeId)
    allItems.foreach(item => removeCartItem(item.getCartItemId))
    cartService.refreshAllCartStates()
  }

  @Transactional(readOnly = true)
  override def getItemById(itemId: Integer): CartItemDto = {
    val itemEntity = itemRepository.findById(itemId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The cart item has not been found!")))
    tempConverter.cartItemEntityToDto(itemEntity)
  }
}
