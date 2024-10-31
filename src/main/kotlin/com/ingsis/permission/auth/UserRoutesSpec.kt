package com.ingsis.permission.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/v1/user")
interface UserRoutesSpec {

  @GetMapping("/id")
  fun getUserId(@RequestParam token: String): String?
}
