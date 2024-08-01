package com.radovan.spring.services

import com.radovan.spring.dto.OrderDto

trait OrderService {

  def listAll:Array[OrderDto]

  def getOrderById(orderId:Integer):OrderDto

  def deleteOrder(orderId:Integer):Unit

  def listAllByCartId(cartId:Integer):Array[OrderDto]

  def addOrder:OrderDto
}
