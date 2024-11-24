package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import com.ingsis.permission.snippetPermissions.SnippetUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PermissionService(
  @Autowired private val snippetPermissionsRepository: SnippetPermissionsRepository
) {

  fun updatePermissions(snippetUser: SnippetUser, newPermission: Permission) {
    val snippetPermissions = getOrCreateSnippetPermissions(snippetUser.snippetId)

    snippetPermissions.applyPermission(snippetUser.userId, newPermission)

    snippetPermissionsRepository.save(snippetPermissions)
  }

  fun getSnippets(userId: String, type: PermissionType): List<String> {
    return snippetPermissionsRepository.findAll()
      .filter { it.hasPermission(userId, type) }
      .map { it.id }
  }

  private fun getOrCreateSnippetPermissions(snippetId: String): SnippetPermissions {
    return snippetPermissionsRepository.findById(snippetId)
      .orElse(SnippetPermissions(id = snippetId, readUsers = mutableListOf(), writeUsers = mutableListOf()))
  }

  fun deleteSnippet(snippetUser: SnippetUser): DeleteResult {
    val snippetPermissions = snippetPermissionsRepository.findById(snippetUser.snippetId).orElse(null)
      ?: return DeleteResult.NOT_FOUND

    val hasWritePermissions = snippetPermissions.writeUsers.contains(snippetUser.userId)
    return if (hasWritePermissions) {
      snippetPermissionsRepository.delete(snippetPermissions)
      DeleteResult.FULLY_DELETED
    } else {
      snippetPermissions.readUsers.remove(snippetUser.userId)
      snippetPermissionsRepository.save(snippetPermissions)
      DeleteResult.PERMISSION_REMOVED
    }
  }
}
