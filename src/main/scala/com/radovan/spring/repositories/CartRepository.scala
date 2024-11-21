package com.radovan.spring.repositories

import com.radovan.spring.entity.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait CartRepository extends JpaRepository[CartEntity, Integer] {

}
