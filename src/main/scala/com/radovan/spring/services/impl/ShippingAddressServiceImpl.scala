package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ShippingAddressDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.ShippingAddressRepository
import com.radovan.spring.services.ShippingAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ShippingAddressServiceImpl extends ShippingAddressService {

  private var addressRepository:ShippingAddressRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(addressRepository: ShippingAddressRepository,tempConverter: TempConverter):Unit = {
    this.addressRepository = addressRepository
    this.tempConverter = tempConverter
  }

  @Transactional
  override def addShippingAddress(address: ShippingAddressDto): ShippingAddressDto = {
    val storedAddress = addressRepository.save(tempConverter.shippingAddressDtoToEntity(address))
    tempConverter.shippingAddressEntityToDto(storedAddress)
  }

  @Transactional(readOnly = true)
  override def getShippingAddress(addressId: Integer): ShippingAddressDto = {
    val addressEntity = addressRepository.findById(addressId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The shipping address has not been found!")))
    tempConverter.shippingAddressEntityToDto(addressEntity)
  }
}
