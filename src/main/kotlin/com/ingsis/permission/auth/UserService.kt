package com.ingsis.permission.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, @Autowired private val authService: AuthService) {

  fun registerUser(accessToken: String, snippetUser: SnippetUser): SnippetUser {
    val snippetUser = SnippetUser(
      auth0id = authService.getUserIdFromToken(accessToken),
      username = snippetUser.username,
      readableSnippets = emptyList<String>(),
      writableSnippets = emptyList<String>()
    )
    return userRepository.save(snippetUser)
  }
}
