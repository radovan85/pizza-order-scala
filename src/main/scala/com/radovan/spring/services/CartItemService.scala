package com.radovan.spring.services

import com.radovan.spring.dto.CartItemDto

trait CartItemService {

  def addCartItem(cartItem:CartItemDto):CartItemDto

  def removeCartItem(itemId:Integer):Unit

  def eraseAllCartItems(cartId:Integer):Unit

  def listAllByCartId(cartId:Integer):Array[CartItemDto]

  def listAllByPizzaSizeId(pizzaSizeId:Integer):Array[CartItemDto]

  def eraseAllByPizzaSizeId(pizzaSizeId:Integer):Unit

  def getItemById(itemId:Integer):CartItemDto
}
