package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.PizzaSizeDto
import com.radovan.spring.exceptions.{ExistingInstanceException, InstanceUndefinedException}
import com.radovan.spring.repositories.PizzaSizeRepository
import com.radovan.spring.services.{CartItemService, CartService, PizzaSizeService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class PizzaSizeServiceImpl extends PizzaSizeService {

  private var pizzaSizeRepository:PizzaSizeRepository = _
  private var tempConverter:TempConverter = _
  private var cartItemService:CartItemService = _
  private var cartService:CartService = _

  @Autowired
  private def injectAll(pizzaSizeRepository: PizzaSizeRepository,tempConverter: TempConverter,
                        cartItemService: CartItemService,cartService: CartService):Unit = {
    this.pizzaSizeRepository = pizzaSizeRepository
    this.tempConverter = tempConverter
    this.cartItemService = cartItemService
    this.cartService = cartService
  }

  @Transactional
  override def addPizzaSize(pizzaSize: PizzaSizeDto): PizzaSizeDto = {
    val sizeId = pizzaSize.getPizzaSizeId
    val existingSize = pizzaSizeRepository.findByNameAndPizzaId(pizzaSize.getName,pizzaSize.getPizzaId).getOrElse(null)
    if(existingSize!=null && sizeId==null){
      throw new ExistingInstanceException(new Error("The size already exists!"))
    }

    val storedSize = pizzaSizeRepository.save(tempConverter.pizzaSizeDtoToEntity(pizzaSize))
    if(sizeId!=null){
      val allCartItems = cartItemService.listAllByPizzaSizeId(sizeId)
      allCartItems.foreach(item => {
        item.setPrice(pizzaSize.getPrice * item.getQuantity)
        cartItemService.addCartItem(item)
      })

      cartService.refreshAllCartStates()
    }

    tempConverter.pizzaSizeEntityToDto(storedSize)

  }

  @Transactional(readOnly = true)
  override def getPizzaSizeById(pizzaSizeId: Integer): PizzaSizeDto = {
    val pizzaSizeEntity = pizzaSizeRepository.findById(pizzaSizeId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The pizza size has not been found!")))
    tempConverter.pizzaSizeEntityToDto(pizzaSizeEntity)
  }

  @Transactional
  override def deletePizzaSize(pizzaSizeId: Integer): Unit = {
    cartItemService.eraseAllByPizzaSizeId(pizzaSizeId)
    pizzaSizeRepository.deleteById(pizzaSizeId)
    pizzaSizeRepository.flush()
    cartService.refreshAllCartStates()
  }

  @Transactional
  override def deleteAllByPizzaId(pizzaId: Integer): Unit = {
    val allPizzaSizes = listAllByPizzaId(pizzaId)
    allPizzaSizes.foreach(pizzaSize => deletePizzaSize(pizzaSize.getPizzaSizeId))
  }

  @Transactional(readOnly = true)
  override def listAll: Array[PizzaSizeDto] = {
    val allPizzaSizes = pizzaSizeRepository.findAll().asScala
    allPizzaSizes.collect{
      case pizzaSizeEntity => tempConverter.pizzaSizeEntityToDto(pizzaSizeEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByPizzaId(pizzaId: Integer): Array[PizzaSizeDto] = {
    val allPizzaSizes = listAll
    allPizzaSizes.collect{
      case pizzaSize if pizzaSize.getPizzaId == pizzaId => pizzaSize
    }
  }
}
