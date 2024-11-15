package com.ingsis.permission.permissions

import com.ingsis.permission.SnippetPermissions.SnippetPermissions
import com.ingsis.permission.SnippetPermissions.SnippetPermissionsRepository
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
      .orElse(SnippetPermissions(id = snippetId, readUsers = mutableListOf(), writeUsers = mutableListOf()))

    when (type) {
      "Read" -> snippetPermissions.addReadPermission(userId)
      "Write" -> snippetPermissions.addWritePermission(userId)
      else -> throw IllegalArgumentException("Unknown permission type: $type")
    }

    snippetPermissionsRepository.save(snippetPermissions)
  }
}
