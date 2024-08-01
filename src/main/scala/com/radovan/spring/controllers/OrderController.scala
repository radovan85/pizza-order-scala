
package com.radovan.spring.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.dto.ShippingAddressDto
import com.radovan.spring.services.{CartItemService, CartService, CustomerService, OrderService, PizzaService, PizzaSizeService, ShippingAddressService, UserService}



@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

   private var cartService:CartService = _
   private var customerService:CustomerService = _
   private var shippingAddressService:ShippingAddressService = _
   private var userService:UserService = _
   private var cartItemService:CartItemService = _
   private var pizzaService:PizzaService = _
   private var pizzaSizeService:PizzaSizeService = _
   private var orderService:OrderService = _

  @Autowired
  private def injectAll(cartService: CartService,customerService: CustomerService,shippingAddressService: ShippingAddressService,
                        userService: UserService,cartItemService: CartItemService,pizzaService: PizzaService,
                        pizzaSizeService: PizzaSizeService,orderService: OrderService):Unit = {

    this.cartService = cartService
    this.customerService = customerService
    this.shippingAddressService = shippingAddressService
    this.userService = userService
    this.cartItemService = cartItemService
    this.pizzaService = pizzaService
    this.pizzaSizeService = pizzaSizeService
    this.orderService = orderService
  }

  @GetMapping(value = Array("/confirmUserData"))
  def confirmData(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val shippingAddress = new ShippingAddressDto
    val currentAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId)
    map.put("shippingAddress", shippingAddress)
    map.put("currentAddress", currentAddress)
    "fragments/userDataConfirm :: fragmentContent"
  }

  @PostMapping(value = Array("/confirmShippingAddress"))
  def confirmShippingAddress(@ModelAttribute("shippingAddress") shippingAddress: ShippingAddressDto): String = {
    shippingAddressService.addShippingAddress(shippingAddress)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/phoneConfirmation"))
  def confirmPhone(map: ModelMap): String = {
    val customer = new CustomerDto
    val authUser = userService.getCurrentUser
    val currentCustomer = customerService.getCustomerByUserId(authUser.getId)
    map.put("customer", customer)
    map.put("currentCustomer", currentCustomer)
    "fragments/phoneConfirmation :: fragmentContent"
  }

  @PostMapping(value = Array("/phoneConfirmation"))
  def confirmPhonePost(@ModelAttribute("customer") customer: CustomerDto): String = {
    customerService.addCustomer(customer)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/orderConfirmation"))
  def renderOrderForm(map: ModelMap): String = {
    val order = new OrderDto
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartById(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(cart.getCartId)
    val allPizzas = pizzaService.listAll
    val allPizzaSizes = pizzaSizeService.listAll
    val shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId)
    map.put("order", order)
    map.put("allCartItems", allCartItems)
    map.put("allPizzas", allPizzas)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("shippingAddress", shippingAddress)
    map.put("cart", cart)
    "fragments/orderConfirmation :: fragmentContent"
  }

  @GetMapping(value = Array("/cancelOrder"))
  def cancelOrder = "fragments/checkOutCancelled :: fragmentContent"

  @GetMapping(value = Array("/createOrder"))
  def addOrder(): String = {
    orderService.addOrder
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/orderCompleted"))
  def orderExecuted = "fragments/orderCompleted :: fragmentContent"
}