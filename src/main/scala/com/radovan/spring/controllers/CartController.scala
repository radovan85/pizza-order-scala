package com.radovan.spring.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.exceptions.CartItemsNumberException
import com.radovan.spring.services.{CartItemService, CartService, CustomerService, PizzaService, PizzaSizeService, UserService}



@Controller
@RequestMapping(value = Array("/carts"))
class CartController {

   private var pizzaService:PizzaService = _
   private var pizzaSizeService:PizzaSizeService = _
   private var cartItemService:CartItemService = _
   private var userService:UserService = _
   private var customerService:CustomerService = _
   private var cartService:CartService = _

  @Autowired
  private def injectAll(pizzaService: PizzaService,pizzaSizeService: PizzaSizeService,cartItemService: CartItemService,
                        userService: UserService,customerService: CustomerService,cartService: CartService):Unit = {

    this.pizzaService = pizzaService
    this.pizzaSizeService = pizzaSizeService
    this.cartItemService = cartItemService
    this.userService = userService
    this.customerService = customerService
    this.cartService = cartService
  }

  @GetMapping(value = Array("/addToCart/{pizzaId}"))
  def addToCart(@PathVariable("pizzaId") pizzaId: Integer, map: ModelMap): String = {
    val cartItem = new CartItemDto
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    val pizza = pizzaService.getPizzaById(pizzaId)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("pizza", pizza)
    map.put("cartItem", cartItem)
    "fragments/addPizzaToCart :: fragmentContent"
  }

  @PostMapping(value = Array("/storeCartItem"))
  def storeCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    val pizzaSizeId = cartItem.getPizzaSizeId
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cartId = customer.getCartId
    cartItem.setCartId(cartId)
    val cartItems = cartItemService.listAllByCartId(cartId)
    var pizzaNumber = cartItem.getQuantity
    cartItems.foreach(itemDto => pizzaNumber = pizzaNumber + itemDto.getQuantity)
    if (pizzaNumber > 20) {
      throw new CartItemsNumberException(new Error("Maximum 20 items allowed in the cart!"))
    }

    cartItems.foreach(itemDto => {
      if(itemDto.getPizzaSizeId == pizzaSizeId){
        val quantity = cartItem.getQuantity + itemDto.getQuantity
        cartItem.setQuantity(quantity)
        cartItem.setCartItemId(itemDto.getCartItemId)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cartId)
        return "fragments/homePage :: fragmentContent"
      }
    })

    cartItemService.addCartItem(cartItem)
    cartService.refreshCartState(cartId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/itemAdded"))
  def itemAddedToCart = "fragments/itemAdded :: fragmentContent"

  @GetMapping(value = Array("/cart"))
  def goToCart(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val cart = cartService.getCartById(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(cart.getCartId)
    val allPizzaSizes = pizzaSizeService.listAll
    val allPizzas = pizzaService.listAll
    map.put("allCartItems", allCartItems)
    map.put("allPizzas", allPizzas)
    map.put("allPizzaSizes", allPizzaSizes)
    map.put("cart", cart)
    "fragments/cart :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteItem/{itemId}"))
  def deleteItem(@PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(itemId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteAllItems/{cartId}"))
  def clearCart(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/validateCart/{cartId}"))
  def cartValidation(@PathVariable("cartId") cartId: Integer): String = {
    cartService.validateCart(cartId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/cartError"))
  def cartWarning = "fragments/invalidCart :: fragmentContent"

  @GetMapping(value = Array("/itemsError"))
  def itemNumberErr = "fragments/itemsNumberError :: fragmentContent"
}