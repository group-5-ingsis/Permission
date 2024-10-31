package com.ingsis.permission.auth

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthService(
  private val restTemplate: RestTemplate,
  @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val authDomain: String
) {

  fun getUserIdFromToken(accessToken: String): String {
    val url = "https://$authDomain/userinfo"
    val headers = HttpHeaders().apply {
      setBearerAuth(accessToken)
    }
    val entity = HttpEntity<Any>(headers)

    val response = restTemplate.exchange(url, HttpMethod.GET, entity, String::class.java)

    return JSONObject(response.body).getString("sub")
  }
}
