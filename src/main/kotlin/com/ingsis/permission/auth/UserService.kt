package com.ingsis.permission.auth

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

  fun saveUserId() {
    val authentication = SecurityContextHolder.getContext().authentication
    val jwt = authentication.principal as Jwt
    val userId = jwt.subject
    userRepository.save(User(auth0id = userId, readableSnippets = emptyList<String>(), writableSnippets = emptyList<String>()))
  }
}
