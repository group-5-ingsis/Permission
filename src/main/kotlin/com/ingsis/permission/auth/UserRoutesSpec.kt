package com.ingsis.permission.auth

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@RequestMapping("/v1/user")
interface UserRoutesSpec {

  @GetMapping("/id")
  fun getUserId(@RequestHeader accessToken: String): String?

  @PostMapping("/")
  fun registerUser(@RequestBody snippetUser: SnippetUser, @RequestParam accessToken: String): SnippetUser
}
