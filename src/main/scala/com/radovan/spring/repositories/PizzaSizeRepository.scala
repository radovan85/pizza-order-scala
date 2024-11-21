package com.radovan.spring.repositories

import com.radovan.spring.entity.PizzaSizeEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait PizzaSizeRepository extends JpaRepository[PizzaSizeEntity, Integer]{

  @Query(value = "select * from pizza_sizes where name = :name and pizza_id = :pizzaId",nativeQuery = true)
  def findByNameAndPizzaId(@Param("name") name:String,@Param("pizzaId") pizzaId:Integer):Option[PizzaSizeEntity]
}
