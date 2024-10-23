package com.ingsis.permission.health

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {

  @GetMapping("/info")
  fun index(): String {
    return "Permission Service"
  }

  @GetMapping("/jwt")
  fun jwt(@AuthenticationPrincipal jwt: Jwt): String {
    return jwt.tokenValue
  }
}
