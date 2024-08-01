package com.radovan.spring.services

import com.radovan.spring.dto.CartDto

trait CartService {

  def refreshCartState(cartId:Integer):Unit

  def refreshAllCartStates():Unit

  def validateCart(cartId:Integer):CartDto

  def getMyCart:CartDto

  def getCartById(cartId:Integer):CartDto
}
