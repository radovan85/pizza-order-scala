package com.radovan.spring.repositories

import com.radovan.spring.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait CustomerRepository extends JpaRepository[CustomerEntity, Integer]{

  def findByUserId(userId:Integer):Option[CustomerEntity]
}
