package com.radovan.spring.controllers

import com.radovan.spring.exceptions.{CartItemsNumberException, ExistingInstanceException, InstanceUndefinedException, InvalidCartException, OperationNotAllowedException, SuspendedUserException}
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}

@ControllerAdvice
class ErrorsController {

  @ExceptionHandler(Array(classOf[InvalidCartException]))
  def handleInvalidCartException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)

  @ExceptionHandler(Array(classOf[ExistingInstanceException]))
  def handleExistingInstanceException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.CONFLICT)

  @ExceptionHandler(Array(classOf[CartItemsNumberException]))
  def handleCartItemsNumberException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.NOT_ACCEPTABLE)

  @ExceptionHandler(Array(classOf[SuspendedUserException]))
  def handleSuspendedUserException(error: Error): ResponseEntity[String] = {
    SecurityContextHolder.clearContext()
    new ResponseEntity[String](error.getMessage, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
  }

  @ExceptionHandler(Array(classOf[InstanceUndefinedException]))
  def handleInstanceUndefinedException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)

  @ExceptionHandler(Array(classOf[OperationNotAllowedException]))
  def handleOperationNotAllowedException(error: Error) = new ResponseEntity[String](error.getMessage, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
}
