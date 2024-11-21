package com.radovan.spring.utils

import com.radovan.spring.dto.{CustomerDto, ShippingAddressDto, UserDto}

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RegistrationForm extends Serializable {

  @BeanProperty var user:UserDto = _
  @BeanProperty var customer:CustomerDto = _
  @BeanProperty var shippingAddress:ShippingAddressDto = _

}
