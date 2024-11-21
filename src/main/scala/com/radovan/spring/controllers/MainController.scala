package com.radovan.spring.controllers

import java.security.Principal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.services.CustomerService
import com.radovan.spring.utils.RegistrationForm


@Controller class MainController {

  @Autowired
  private var customerService: CustomerService = _

  @GetMapping(value = Array("/"))
  def indexPage = "index"

  @GetMapping(value = Array("/home"))
  def home = "fragments/homePage :: fragmentContent"

  @GetMapping(value = Array("/login"))
  def login = "fragments/login :: fragmentContent"

  @PostMapping(value = Array("/loginPassConfirm"))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipalOption = Option(principal)
    authPrincipalOption match {
      case Some(_) =>
      case None => throw new InstanceUndefinedException(new Error("Invalid user!"))
    }

    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/loginErrorPage"))
  def loginError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password")
    "fragments/login :: fragmentContent"
  }

  @PostMapping(value = Array("/loggedout"))
  def logout: String = {
    val context = SecurityContextHolder.getContext
    context.setAuthentication(null)
    SecurityContextHolder.clearContext()
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/register"))
  def renderRegistrationForm(map: ModelMap): String = {
    val registerForm = new RegistrationForm
    map.put("registerForm", registerForm)
    "fragments/registrationForm :: fragmentContent"
  }

  @PostMapping(value = Array("/register"))
  def storeCustomer(@ModelAttribute("registerForm") registerForm: RegistrationForm): String = {
    customerService.storeCustomer(registerForm)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/registerComplete"))
  def registrationCompleted = "fragments/registration_completed :: fragmentContent"

  @GetMapping(value = Array("/registerFail"))
  def registrationFailed = "fragments/registration_failed :: fragmentContent"

}
