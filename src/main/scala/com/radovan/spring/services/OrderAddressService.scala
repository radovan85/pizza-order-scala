package com.radovan.spring.services

import com.radovan.spring.dto.OrderAddressDto

trait OrderAddressService {

  def getAddressById(addressId:Integer):OrderAddressDto
}
