package com.radovan.spring.controllers

import com.radovan.spring.dto.{PizzaDto, PizzaSizeDto}
import com.radovan.spring.services.{CustomerService, OrderAddressService, OrderItemService, OrderService, PizzaService, PizzaSizeService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  private var pizzaService: PizzaService = _
  private var pizzaSizeService: PizzaSizeService = _
  private var customerService: CustomerService = _
  private var userService: UserService = _
  private var orderService: OrderService = _
  private var orderAddressService: OrderAddressService = _
  private var orderItemService: OrderItemService = _

  @Autowired
  private def injectAll(pizzaService: PizzaService, pizzaSizeService: PizzaSizeService, customerService: CustomerService,
                        userService: UserService, orderService: OrderService, orderAddressService: OrderAddressService,
                        orderItemService: OrderItemService): Unit = {

    this.pizzaService = pizzaService
    this.pizzaSizeService = pizzaSizeService
    this.customerService = customerService
    this.userService = userService
    this.orderService = orderService
    this.orderAddressService = orderAddressService
    this.orderItemService = orderItemService
  }

  @GetMapping(value = Array("/createPizza"))
  def renderPizzaForm(map: ModelMap): String = {
    val pizza = new PizzaDto
    map.put("pizza", pizza)
    "fragments/pizzaForm :: fragmentContent"
  }

  @GetMapping(value = Array("/allPizzas"))
  def listAllPizzas(map: ModelMap): String = {
    val allPizzas = pizzaService.listAll
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaList :: fragmentContent"
  }

  @GetMapping(value = Array("/invalidPath"))
  def invalidImagePath = "fragments/invalidImagePath :: fragmentContent"

  @PostMapping(value = Array("/createPizza"))
  def createBook(@ModelAttribute("pizza") pizza: PizzaDto): String = {
    pizzaService.addPizza(pizza)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/pizzaDetails/{pizzaId}"))
  def getPizzaDetails(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("pizza", pizza)
    "fragments/pizzaDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/updatePizza/{pizzaId}"))
  def renderUpdatePizzaForm(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val pizza = new PizzaDto
    val currentPizza = pizzaService.getPizzaById(pizzaId)
    map.put("pizza", pizza)
    map.put("currentPizza", currentPizza)
    "fragments/updatePizzaForm :: fragmentContent"
  }

  @GetMapping(value = Array("/deletePizza/{pizzaId}"))
  def deletePizza(@PathVariable("pizzaId") pizzaId: Integer): String = {
    pizzaService.deletePizza(pizzaId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/sizeList/{pizzaId}"))
  def getSizeList(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    "fragments/pizzaSizesForPizza :: fragmentContent"
  }

  @GetMapping(value = Array("/allSizes"))
  def getAllPizzaSizes(map: ModelMap): String = {
    val allPizzaSizes = pizzaSizeService.listAll
    val allPizzas = pizzaService.listAll
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("allPizzas", allPizzas)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaSizeList :: fragmentContent"
  }

  @GetMapping(value = Array("/allSizes/{pizzaId}"))
  def allSizesByPizzaId(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("pizza", pizza)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/pizzaSizeListByPizza :: fragmentContent"
  }

  @GetMapping(value = Array("/createPizzaSize")) def renderPizzaSizeForm(map: ModelMap): String = {
    val pizzaSize = new PizzaSizeDto
    val allPizzas = pizzaService.listAll
    map.put("pizzaSize", pizzaSize)
    map.put("allPizzas", allPizzas)
    "fragments/pizzaSizeForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createPizzaSize"))
  def storePizzaSize(@ModelAttribute("pizzaSize") pizzaSize: PizzaSizeDto): String = {
    pizzaSizeService.addPizzaSize(pizzaSize)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/updatePizzaSize/{pizzaSizeId}"))
  def renderUpdatePizzaSizeForm(@PathVariable("pizzaSizeId") pizzaSizeId: Integer, map: ModelMap): String = {
    val pizzaSize = new PizzaSizeDto
    val currentPizzaSize = pizzaSizeService.getPizzaSizeById(pizzaSizeId)
    val allPizzas = pizzaService.listAll
    map.put("pizzaSize", pizzaSize)
    map.put("allPizzas", allPizzas)
    map.put("currentPizzaSize", currentPizzaSize)
    "fragments/updatePizzaSizeForm :: fragmentContent"
  }

  @GetMapping(value = Array("/deletePizzaSize/{pizzaSizeId}"))
  def deletePizzaSize(@PathVariable("pizzaSizeId") pizzaSizeId: Integer): String = {
    pizzaSizeService.deletePizzaSize(pizzaSizeId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/pizzaSizeDetails/{pizzaSizeId}"))
  def getPizzaSizeDetails(@PathVariable("pizzaSizeId") pizzaSizeId: Integer, map: ModelMap): String = {
    val pizzaSize = pizzaSizeService.getPizzaSizeById(pizzaSizeId)
    val pizza = pizzaService.getPizzaById(pizzaSize.getPizzaId)
    map.put("pizzaSize", pizzaSize)
    map.put("pizza", pizza)
    "fragments/pizzaSizeDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/allCustomers"))
  def getAllCustomers(map: ModelMap): String = {
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 8.asInstanceOf[Integer])
    "fragments/customerList :: fragmentContent"
  }

  @GetMapping(value = Array("/customerDetails/{customerId}"))
  def getCustomerDetails(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer = customerService.getCustomerById(customerId)
    val user = userService.getUserById(customer.getUserId)
    map.put("customer", customer)
    map.put("user", user)
    "fragments/customerDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/suspendUser/{userId}"))
  def suspendUser(@PathVariable("userId") userId: Integer): String = {
    userService.suspendUser(userId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/clearSuspension/{userId}"))
  def removeSuspension(@PathVariable("userId") userId: Integer): String = {
    userService.clearSuspension(userId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allOrders"))
  def listAllOrders(map: ModelMap): String = {
    val allOrders = orderService.listAll
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 8.asInstanceOf[Integer])
    "fragments/orderList :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteOrder/{orderId}"))
  def removeOrder(@PathVariable("orderId") orderId: Integer): String = {
    orderService.deleteOrder(orderId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/orderDetails/{orderId}"))
  def getOrderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order = orderService.getOrderById(orderId)
    val address = orderAddressService.getAddressById(order.getAddressId)
    val orderedItems = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderedItems", orderedItems)
    "fragments/orderDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/existingSizeError"))
  def sizeError = "fragments/pizzaSizeError :: fragmentContent"

  @GetMapping(value = Array("/deleteCustomer/{customerId}"))
  def removeCustomer(@PathVariable("customerId") customerId: Integer): String = {
    customerService.deleteCustomer(customerId)
    "fragments/homePage :: fragmentContent"
  }
}
