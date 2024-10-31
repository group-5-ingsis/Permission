package com.ingsis.permission.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class UserRoutes(@Autowired val authService: AuthService) : UserRoutesSpec {

  override fun getUserId(accessToken: String): String? {
    return authService.getUserIdFromToken(accessToken)
  }
}
