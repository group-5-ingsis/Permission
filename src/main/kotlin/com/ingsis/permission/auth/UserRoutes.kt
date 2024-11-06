package com.ingsis.permission.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/user")
@RestController
class UserRoutes(@Autowired val authService: AuthService) {

  @GetMapping("/id")
  fun getUserId(@RequestHeader("Authorization") token: String): String {
    return authService.getUserId(token)
  }
}
