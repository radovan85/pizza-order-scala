package com.radovan.spring.repositories

import com.radovan.spring.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait OrderRepository extends JpaRepository[OrderEntity, Integer]{

}
