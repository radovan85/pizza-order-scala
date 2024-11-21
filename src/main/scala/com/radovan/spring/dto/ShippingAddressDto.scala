package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ShippingAddressDto extends Serializable {

  @BeanProperty var shippingAddressId: Integer = _

  @BeanProperty var address: String = _

  @BeanProperty var city: String = _

  @BeanProperty var postcode: String = _

  @BeanProperty var customerId: Integer = _
}
