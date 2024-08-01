package com.radovan.spring.services

import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.utils.RegistrationForm

trait CustomerService {

  def storeCustomer(form:RegistrationForm):CustomerDto

  def getAllCustomers():Array[CustomerDto]

  def getCustomerById(customerId:Integer):CustomerDto

  def getCustomerByUserId(userId:Integer):CustomerDto

  def addCustomer(customer: CustomerDto):CustomerDto

  def deleteCustomer(customerId:Integer):Unit

  def getCurrentCustomer:CustomerDto
}
