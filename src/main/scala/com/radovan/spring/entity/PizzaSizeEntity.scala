package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "pizza_sizes")
@SerialVersionUID(1L)
class PizzaSizeEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "pizza_size_id")
  @BeanProperty var pizzaSizeId:Integer = _

  @Column(nullable = false, length = 40)
  @BeanProperty var name:String = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "pizza_id", nullable = false)
  @BeanProperty var pizza:PizzaEntity = _
}
