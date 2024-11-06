package com.ingsis.permission.permissions

import com.ingsis.permission.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(@Autowired private val userRepository: UserRepository) {

  fun getReadableSnippets(userId: String): List<String> {
    val user = userRepository.findByAuth0id(userId)
    return user?.readableSnippets ?: emptyList()
  }
}
