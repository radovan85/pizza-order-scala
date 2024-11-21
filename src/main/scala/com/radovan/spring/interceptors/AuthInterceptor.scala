package com.radovan.spring.interceptors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import com.radovan.spring.dto.UserDto
import com.radovan.spring.exceptions.SuspendedUserException
import com.radovan.spring.services.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class AuthInterceptor extends HandlerInterceptor {

  @Autowired
  private var userService: UserService = _

  @throws[Exception]
  override def preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = {
    var authUser = new UserDto
    authUser.setEnabled(1.toShort)
    try {
      val authUserOpt: Option[UserDto] = Option(userService.getCurrentUser)
      authUser = authUserOpt.getOrElse(authUser)
    } catch {
      case _: Exception =>
    }
    if (authUser.getEnabled == 0) {
      throw new SuspendedUserException(new Error("Account suspended"))
    }
    true
  }

}
