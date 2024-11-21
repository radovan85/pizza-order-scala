package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name="order_addresses")
@SerialVersionUID(1L)
class OrderAddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id")
  @BeanProperty var orderAddressId:Integer = _

  @Column(nullable = false, length = 75)
  @BeanProperty var address:String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var city:String = _

  @Column(name = "post_code", nullable = false, length = 10)
  @BeanProperty var postcode:String = _

  @OneToOne(mappedBy = "address", fetch = FetchType.EAGER, orphanRemoval = true)
  @BeanProperty var order:OrderEntity = _
}
