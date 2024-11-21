package com.radovan.spring.services

import com.radovan.spring.dto.PizzaSizeDto

trait PizzaSizeService {

  def addPizzaSize(pizzaSize:PizzaSizeDto):PizzaSizeDto

  def getPizzaSizeById(pizzaSizeId:Integer):PizzaSizeDto

  def deletePizzaSize(pizzaSizeId:Integer):Unit

  def deleteAllByPizzaId(pizzaId:Integer):Unit

  def listAll:Array[PizzaSizeDto]

  def listAllByPizzaId(pizzaId:Integer):Array[PizzaSizeDto]
}
