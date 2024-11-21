package com.radovan.spring.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.services.{PizzaService, PizzaSizeService}


@Controller
@RequestMapping(value = Array("/pizzas"))
class PizzaController {

  private var pizzaService: PizzaService = _
  private var pizzaSizeService: PizzaSizeService = _

  @Autowired
  private def injectAll(pizzaService: PizzaService, pizzaSizeService: PizzaSizeService): Unit = {
    this.pizzaService = pizzaService
    this.pizzaSizeService = pizzaSizeService
  }

  @GetMapping(value = Array("/allPizzas"))
  def listAllPizzas(map: ModelMap): String = {
    val allPizzas = pizzaService.listAll
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaListUser :: fragmentContent"
  }

  @GetMapping(value = Array("/pizzaDetails/{pizzaId}"))
  def getPizzaDetails(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val pizza = pizzaService.getPizzaById(pizzaId)
    val pizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    map.put("pizza", pizza)
    map.put("pizzaSizes", pizzaSizes)
    "fragments/pizzaDetailsUser :: fragmentContent"
  }

  @GetMapping(value = Array("/orderPizza/{pizzaId}"))
  def orderPizza(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val cartItem = new CartItemDto
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("pizza", pizza)
    map.put("cartItem", cartItem)
    "fragments/orderPizza :: fragmentContent"
  }

  @GetMapping(value = Array("/searchPizza/{keyword}"))
  def searchPizza(@PathVariable("keyword") keyword: String, map: ModelMap): String = {
    val allPizzas = pizzaService.listAllByKeyword(keyword)
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/searchResult :: fragmentContent"
  }
}
