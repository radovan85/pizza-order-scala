package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, Table, Transient}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "pizzas")
@SerialVersionUID(1L)
class PizzaEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "pizza_id")
  @BeanProperty var pizzaId:Integer = _

  @Column(nullable = false, length = 40)
  @BeanProperty var name:String = _

  @Column(nullable = false, length = 90)
  @BeanProperty var description:String = _

  @Transient
  @OneToMany(mappedBy = "pizza", orphanRemoval = true, fetch = FetchType.EAGER)
  @BeanProperty var pizzaSizes:util.List[PizzaSizeEntity] = _

  @Column(name = "image_url", nullable = false, length = 255)
  @BeanProperty var imageUrl:String = _
}
