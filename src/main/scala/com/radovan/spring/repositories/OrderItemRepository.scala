package com.radovan.spring.repositories

import com.radovan.spring.entity.OrderItemEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait OrderItemRepository extends JpaRepository[OrderItemEntity, Integer]{

  @Query(value="select sum(price) from order_items where order_id = :orderId",nativeQuery = true)
  def calculateOrderTotal(@Param("orderId") orderId:Integer):Option[Float]
}
