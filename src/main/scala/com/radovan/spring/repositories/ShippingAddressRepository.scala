package com.radovan.spring.repositories

import com.radovan.spring.entity.ShippingAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait ShippingAddressRepository extends JpaRepository[ShippingAddressEntity, Integer]{

}
