package com.radovan.spring.repositories

import com.radovan.spring.entity.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait CartItemRepository extends JpaRepository[CartItemEntity, Integer]{

}
