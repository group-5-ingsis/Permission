package com.ingsis.permission.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/v1/user")
interface UserRoutesSpec {

  @GetMapping("/id")
  fun getUserId(@RequestHeader token: String): String?
}
