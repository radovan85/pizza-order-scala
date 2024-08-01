package com.radovan.spring.services

import com.radovan.spring.dto.UserDto

trait UserService {

  def getUserById(userId:Integer):UserDto

  def listAllUsers:Array[UserDto]

  def getUserByEmail(email:String):UserDto

  def getCurrentUser:UserDto

  def suspendUser(userId:Integer):Unit

  def clearSuspension(userId:Integer):Unit

  def isAdmin:Boolean
}
