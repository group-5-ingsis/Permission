package com.ingsis.permission.permissions

import com.ingsis.permission.user.SnippetUser
import com.ingsis.permission.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(@Autowired private val userRepository: UserRepository) {

  fun getReadableSnippets(userId: String): List<String> {
    val user = getUser(userId)
    return user?.readableSnippets ?: emptyList()
  }

  private fun getUser(userId: String): SnippetUser? {
    return userRepository.findByAuth0id(userId)
  }

  fun getWritableSnippets(userId: String): List<String> {
    val user = getUser(userId)
    return user?.writableSnippets ?: emptyList()
  }
}
