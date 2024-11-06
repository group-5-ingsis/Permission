package com.ingsis.permission.permissions

import com.ingsis.permission.user.SnippetUser
import com.ingsis.permission.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(@Autowired private val userRepository: UserRepository) {

  fun getSnippets(userId: String, type: String): List<String> {
    val user = checkUserExists(userId)
    return when (type) {
      "Read" -> user?.readableSnippets ?: emptyList()
      "Write" -> user?.writableSnippets ?: emptyList()
      else -> emptyList()
    }
  }

  private fun checkUserExists(userId: String): SnippetUser? {
    val user = userRepository.findByAuth0id(userId)
    if (user == null) {
      val newUser = SnippetUser(
        auth0id = userId,
        username = getAuth0Username(),
        readableSnippets = emptyList(),
        writableSnippets = emptyList()
      )
      return userRepository.save(newUser)
    }
    return user
  }

  // Agarrar el username con /tokenInfo de auth0
  private fun getAuth0Username(): String {
    return "hello"
  }

  fun updatePermission(userId: String, snippetId: String, type: String): SnippetUser {
    val user = getUser(userId) ?: throw IllegalArgumentException("User not found")

    when (type) {
      "Read" -> {
        val alreadyCanRead = user.readableSnippets.contains(snippetId)
        if (!alreadyCanRead) {
          user.readableSnippets = user.readableSnippets + snippetId
        }
      }
      "Write" -> {
        val alreadyCanWrite = user.writableSnippets.contains(snippetId)
        if (!alreadyCanWrite) {
          user.writableSnippets = user.writableSnippets + snippetId
        }
      }
      else -> throw IllegalArgumentException("Unknown permission type")
    }
    return userRepository.save(user)
  }

  private fun getUser(userId: String): SnippetUser? {
    return userRepository.findByAuth0id(userId)
  }
}
