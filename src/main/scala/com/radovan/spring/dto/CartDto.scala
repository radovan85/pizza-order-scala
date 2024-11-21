package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartDto extends Serializable {

  @BeanProperty var cartId: Integer = _

  @BeanProperty var customerId: Integer = _

  @BeanProperty var cartItemsIds: Array[Integer] = _

  @BeanProperty var cartPrice: Float = _
}
