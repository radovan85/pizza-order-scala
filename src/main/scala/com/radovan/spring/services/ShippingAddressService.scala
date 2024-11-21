package com.radovan.spring.services

import com.radovan.spring.dto.ShippingAddressDto

trait ShippingAddressService {

  def addShippingAddress(address:ShippingAddressDto):ShippingAddressDto

  def getShippingAddress(addressId:Integer):ShippingAddressDto
}
