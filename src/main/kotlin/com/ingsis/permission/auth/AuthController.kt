package com.ingsis.permission.auth

import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
  @Value("\${auth0.domain}") private val auth0Domain: String,
  @Value("\${auth0.client-id}") private val clientId: String,
  @Value("\${auth0.redirect-uri}") private val redirectUri: String
) {

  @GetMapping("/register")
  fun register(response: HttpServletResponse) {
    val registerUrl = "https://$auth0Domain/authorize" +
      "?client_id=$clientId" +
      "&redirect_uri=$redirectUri" +
      "&response_type=code" +
      "&scope=openid profile email" +
      "&screen_hint=signup"

    response.sendRedirect(registerUrl)
  }

  @GetMapping("/login")
  fun login(response: HttpServletResponse) {
    val loginUrl = "https://$auth0Domain/authorize" +
      "?client_id=$clientId" +
      "&redirect_uri=$redirectUri" +
      "&response_type=code" +
      "&scope=openid profile email"

    response.sendRedirect(loginUrl)
  }
}
