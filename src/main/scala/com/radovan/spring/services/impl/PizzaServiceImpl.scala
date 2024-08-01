package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.PizzaDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.PizzaRepository
import com.radovan.spring.services.{PizzaService, PizzaSizeService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class PizzaServiceImpl extends PizzaService {

  private var pizzaRepository:PizzaRepository = _
  private var tempConverter:TempConverter = _
  private var pizzaSizeService:PizzaSizeService = _

  @Autowired
  private def injectAll(pizzaRepository: PizzaRepository,tempConverter: TempConverter,
                        pizzaSizeService: PizzaSizeService):Unit = {
    this.pizzaRepository = pizzaRepository
    this.tempConverter = tempConverter
    this.pizzaSizeService = pizzaSizeService
  }

  @Transactional(readOnly = true)
  override def listAll: Array[PizzaDto] = {
    val allPizzas = pizzaRepository.findAll().asScala
    allPizzas.collect{
      case pizzaEntity => tempConverter.pizzaEntityToDto(pizzaEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getPizzaById(pizzaId: Integer): PizzaDto = {
    val pizzaEntity = pizzaRepository.findById(pizzaId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The pizza has not been found!")))
    tempConverter.pizzaEntityToDto(pizzaEntity)
  }

  @Transactional
  override def deletePizza(pizzaId: Integer): Unit = {
    getPizzaById(pizzaId)
    val allPizzaSizes = pizzaSizeService.listAllByPizzaId(pizzaId)
    allPizzaSizes.foreach(pizzaSize => pizzaSizeService.deletePizzaSize(pizzaSize.getPizzaSizeId))
    pizzaRepository.deleteById(pizzaId)
    pizzaRepository.flush()
  }

  @Transactional
  override def addPizza(pizza: PizzaDto): PizzaDto = {
    val storedPizza = pizzaRepository.save(tempConverter.pizzaDtoToEntity(pizza))
    tempConverter.pizzaEntityToDto(storedPizza)
  }

  @Transactional(readOnly = true)
  override def listAllByKeyword(keyword: String): Array[PizzaDto] = {
    val allPizzas = pizzaRepository.findAllByKeyword(keyword).asScala
    allPizzas.collect{
      case pizzaEntity => tempConverter.pizzaEntityToDto(pizzaEntity)
    }.toArray
  }
}
