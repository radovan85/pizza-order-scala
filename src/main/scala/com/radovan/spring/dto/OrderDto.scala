package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderDto extends Serializable {

  @BeanProperty var orderId: Integer = _

  @BeanProperty var orderPrice: Float = _

  @BeanProperty var cartId:Integer  = _

  @BeanProperty var createTime: String = _

  @BeanProperty var orderedItemsIds: Array[Integer] = _

  @BeanProperty var addressId: Integer = _
}
