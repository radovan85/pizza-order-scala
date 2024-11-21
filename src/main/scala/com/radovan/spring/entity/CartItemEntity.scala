package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "cart_items")
@SerialVersionUID(1L)
class CartItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var cartItemId:Integer = _

  @Column(nullable = false)
  @BeanProperty var quantity:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "size_id", nullable = false)
  @BeanProperty var pizzaSize:PizzaSizeEntity = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id", nullable = false)
  @BeanProperty var cart:CartEntity = _
}
