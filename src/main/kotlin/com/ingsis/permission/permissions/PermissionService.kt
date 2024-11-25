package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(
  @Autowired private val snippetPermissionsRepository: SnippetPermissionsRepository
) {

  fun updatePermissions(snippetUser: SnippetUser, newPermissionChange: PermissionChange) {
    val snippetPermissions = getOrCreateSnippetPermissions(snippetUser.snippetId)

    snippetPermissions.updatePermission(snippetUser.userId, newPermissionChange)

    snippetPermissionsRepository.save(snippetPermissions)
  }

  fun hasPermission(snippetUser: SnippetUser, type: String): Boolean {
    val snippet = getOrCreateSnippetPermissions(snippetUser.snippetId)
    return snippet.hasPermission(snippetUser.userId, type)
  }

  fun getSnippets(userId: String, type: String): List<String> {
    return snippetPermissionsRepository.findAll()
      .filter { it.hasPermission(userId, type) }
      .map { it.id }
  }

  private fun getOrCreateSnippetPermissions(snippetId: String): SnippetPermissions {
    return snippetPermissionsRepository.findById(snippetId)
      .orElse(SnippetPermissions(id = snippetId, readUsers = mutableListOf(), writeUsers = mutableListOf()))
  }
}
