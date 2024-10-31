package com.ingsis.permission.auth

import org.springframework.web.bind.annotation.RestController

@RestController
class UserRoutes : UserRoutesSpec {

  override fun getUserId(token: String): String? {
    TODO("Not yet implemented")
  }
}
