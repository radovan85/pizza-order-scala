package com.radovan.spring.exceptions

import javax.management.RuntimeErrorException

@SerialVersionUID(1L)
class OperationNotAllowedException(val error: Error) extends RuntimeErrorException(error){

}
