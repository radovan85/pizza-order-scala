package com.radovan.spring.repositories

import com.radovan.spring.entity.CartItemEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util

@Repository
trait CartItemRepository extends JpaRepository[CartItemEntity, Integer]{

  @Query(value = "select * from cart_items where cart_id = :cartId",nativeQuery = true)
  def findAllByCartId(@Param("cartId") cartId:Integer):util.List[CartItemEntity]

  @Modifying
  @Query(value = "delete from cart_items where cart_id = :cartId",nativeQuery = true)
  def deleteAllByCartId(@Param("cartId") cartId:Integer):Unit

  @Query(value = "select * from cart_items where size_id = :pizzaSizeId",nativeQuery = true)
  def findAllByPizzaSizeId(@Param("pizzaSizeId") pizzaSizeId:Integer):util.List[CartItemEntity]

  @Modifying
  @Query(value = "delete from cart_items where size_id = :pizzaSizeId",nativeQuery = true)
  def deleteAllByPizzaSizeId(@Param("pizzaSizeId") pizzaSizeId:Integer):Unit

  @Query(value = "select sum(price) from cart_items where cart_id = :cartId", nativeQuery = true)
  def calculateGrandTotal(@Param("cartId") cartId: Integer): Option[Float]

  @Modifying
  @Query(value = "delete from cart_items where id = :itemId", nativeQuery = true)
  def eraseCartItem(@Param("itemId") itemId:Integer):Unit
}
