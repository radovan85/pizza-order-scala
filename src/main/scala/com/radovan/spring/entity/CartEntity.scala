package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, OneToOne, Table}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "carts")
@SerialVersionUID(1L)
class CartEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  @BeanProperty var cartId:Integer = _

  @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true,mappedBy = "cart")
  @BeanProperty var customer:CustomerEntity = _

  @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, orphanRemoval = true)
  @BeanProperty var cartItems: util.List[CartItemEntity] = _

  @Column(name = "cart_price", nullable = false)
  @BeanProperty var cartPrice: Float = _
}
