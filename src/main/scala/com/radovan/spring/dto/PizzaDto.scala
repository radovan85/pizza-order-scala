package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class PizzaDto extends Serializable {

  @BeanProperty var pizzaId: Integer = _

  @BeanProperty var name: String = _

  @BeanProperty var description: String = _

  @BeanProperty var pizzaSizesIds: Array[Integer] = _

  @BeanProperty var imageUrl: String = _
}
