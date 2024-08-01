package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderAddressDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.OrderAddressRepository
import com.radovan.spring.services.OrderAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderAddressServiceImpl extends OrderAddressService {

  private var addressRepository:OrderAddressRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(addressRepository: OrderAddressRepository,tempConverter: TempConverter):Unit = {
    this.addressRepository = addressRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getAddressById(addressId: Integer): OrderAddressDto = {
    val addressEntity = addressRepository.findById(addressId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The order address has not been found!")))
    tempConverter.orderAddressEntityToDto(addressEntity)
  }
}
