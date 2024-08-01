package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.UserDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repositories.{RoleRepository, UserRepository}
import com.radovan.spring.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class UserServiceImpl extends UserService {

  private var userRepository:UserRepository = _
  private var tempConverter:TempConverter = _
  private var roleRepository:RoleRepository = _

  @Autowired
  private def injectAll(userRepository: UserRepository,tempConverter: TempConverter,
                        roleRepository: RoleRepository):Unit = {
    this.userRepository = userRepository
    this.tempConverter = tempConverter
    this.roleRepository = roleRepository
  }

  @Transactional(readOnly = true)
  override def getUserById(userId: Integer): UserDto = {
    val userEntity = userRepository.findById(userId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The user has not been found!")))
    tempConverter.userEntityToDto(userEntity)
  }

  @Transactional(readOnly = true)
  override def listAllUsers: Array[UserDto] = {
    val allUsers = userRepository.findAll().asScala
    allUsers.collect{
      case userEntity => tempConverter.userEntityToDto(userEntity)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getUserByEmail(email: String): UserDto = {
    val userOption = userRepository.findByEmail(email)
    userOption match {
      case Some(userEntity) => tempConverter.userEntityToDto(userEntity)
      case None => throw new InstanceUndefinedException(new Error("The user has not been found"))
    }
  }

  @Transactional(readOnly = true)
  override def getCurrentUser: UserDto = {
    val authentication = SecurityContextHolder.getContext.getAuthentication
    if (authentication.isAuthenticated) {
      val currentUsername = authentication.getName
      userRepository.findByEmail(currentUsername)
        .map(tempConverter.userEntityToDto)
        .getOrElse(throw new InstanceUndefinedException(new Error("Invalid user!")))
    } else {
      throw new InstanceUndefinedException(new Error("Invalid user!"))
    }
  }

  @Transactional
  override def suspendUser(userId: Integer): Unit = {
    val user = getUserById(userId)
    user.setEnabled(0.asInstanceOf[Short])
    userRepository.saveAndFlush(tempConverter.userDtoToEntity(user))
  }

  @Transactional
  override def clearSuspension(userId: Integer): Unit = {
    val user = getUserById(userId)
    user.setEnabled(1.asInstanceOf[Short])
    userRepository.saveAndFlush(tempConverter.userDtoToEntity(user))
  }

  @Transactional(readOnly = true)
  override def isAdmin: Boolean = {
    val authUser = getCurrentUser
    roleRepository.findByRole("ADMIN") match {
      case Some(role) => authUser.getRolesIds.contains(role.getId)
      case None => false
    }
  }
}
