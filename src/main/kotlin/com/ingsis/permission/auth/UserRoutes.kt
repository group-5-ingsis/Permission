package com.ingsis.permission.auth

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/user")
@RestController
class UserRoutes {

  @GetMapping("/id")
  fun getUserId(@AuthenticationPrincipal jwt: Jwt): String {
    return jwt.subject
  }
}
