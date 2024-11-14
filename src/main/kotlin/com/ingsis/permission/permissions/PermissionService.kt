package com.ingsis.permission.permissions

import com.ingsis.permission.user.SnippetUser
import com.ingsis.permission.user.UserDto
import com.ingsis.permission.user.UserRepository
import com.ingsis.permission.user.toUserDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(
  @Autowired private val userRepository: UserRepository
) {

  private val logger = LoggerFactory.getLogger(PermissionController::class.java)

  fun getSnippets(userId: String, username: String, type: String): List<String> {
    val user = getOrCreateUser(userId, username)
    return when (type) {
      "Read" -> user.readableSnippets
      "Write" -> user.writableSnippets
      else -> emptyList()
    }
  }

  private fun getOrCreateUser(userId: String, username: String): SnippetUser {
    val user = userRepository.findByAuth0id(userId) ?: createUser(userId, username)
    return user
  }

  private fun createUser(userId: String, email: String): SnippetUser {
    val newUser = SnippetUser(
      auth0id = userId,
      username = email,
      readableSnippets = emptyList(),
      writableSnippets = emptyList()
    )
    return userRepository.save(newUser)
  }

  fun getUsers(): List<UserDto> {
    val users = userRepository.findAll()
    logger.info("Returning users list: $users")

    return users.mapNotNull {
      logger.info("User Name: ${it.username}")

      it.toUserDto()
    }
  }

  fun updatePermission(userId: String, username: String, snippetId: String, type: String) {
    val user = getOrCreateUser(userId, username)

    when (type) {
      "Read" -> user.readableSnippets = updatePermissions(user.readableSnippets, snippetId)
      "Write" -> user.writableSnippets = updatePermissions(user.writableSnippets, snippetId)
      else -> throw IllegalArgumentException("Unknown permission type")
    }

    userRepository.save(user)
  }

  private fun updatePermissions(snippetList: List<String>, snippetId: String): List<String> {
    return if (snippetList.contains(snippetId)) snippetList else snippetList + snippetId
  }
}
