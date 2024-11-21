package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name="order_items")
@SerialVersionUID(1L)
class OrderItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  @BeanProperty var orderItemId:Integer = _

  @Column(nullable = false)
  @BeanProperty var quantity:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(nullable = false)
  @BeanProperty var pizza:String = _

  @Column(name = "pizza_size",nullable = false)
  @BeanProperty var pizzaSize:String = _

  @Column(name="pizza_price",nullable = false)
  @BeanProperty var pizzaPrice:Float = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id")
  @BeanProperty var order:OrderEntity = _
}
