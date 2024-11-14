package com.ingsis.permission.permissions

import com.ingsis.permission.user.SnippetPermissionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(
  @Autowired private val snippetPermissionsRepository: SnippetPermissionsRepository
) {

  fun getSnippets(userId: String, type: String): List<String> {
    return when (type) {
      "read" -> getReadableSnippets(userId)
      "write" -> getWritableSnippets(userId)
      else -> emptyList()
    }
  }

  private fun getWritableSnippets(userId: String): List<String> {
    val snippets = snippetPermissionsRepository.findAll()
      .filter { it.readUsers.contains(userId) }
      .map { it.id }
    return snippets
  }

  private fun getReadableSnippets(userId: String): List<String> {
    val snippets = snippetPermissionsRepository.findAll()
      .filter { it.readUsers.contains(userId) }
      .map { it.id }
    return snippets
  }

  fun updatePermission(userId: String, snippetId: String, type: String) {
    val snippetPermissions = snippetPermissionsRepository.findById(snippetId)
      .orElseThrow { IllegalArgumentException("Snippet with ID $snippetId not found") }

    when (type) {
      "Read" -> snippetPermissions.addReadPermission(userId)
      "Write" -> snippetPermissions.addWritePermission(userId)
      else -> throw IllegalArgumentException("Unknown permission type: $type")
    }

    snippetPermissionsRepository.save(snippetPermissions)
  }
}
