package com.radovan.spring.repositories

import com.radovan.spring.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait OrderItemRepository extends JpaRepository[OrderItemEntity, Integer]{

}
