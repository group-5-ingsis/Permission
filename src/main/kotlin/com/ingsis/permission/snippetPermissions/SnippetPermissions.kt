package com.ingsis.permission.snippetPermissions

import com.ingsis.permission.permissions.PermissionChange
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id

@Entity
data class SnippetPermissions(
  @Id
  var id: String,

  @ElementCollection(fetch = FetchType.EAGER)
  var readUsers: MutableList<String> = mutableListOf(),

  @ElementCollection(fetch = FetchType.EAGER)
  var writeUsers: MutableList<String> = mutableListOf()
) {
  constructor() : this(id = "")

  companion object {
    object PermissionType {
      const val READ = "read"
      const val WRITE = "write"
    }

    object Operation {
      const val REMOVE = "remove"
      const val ADD = "add"
    }
  }

  fun updatePermission(userId: String, newPermission: PermissionChange) {
    val existingPermissions = when (newPermission.permissionType) {
      PermissionType.READ -> readUsers
      PermissionType.WRITE -> writeUsers
      else -> { throw IllegalArgumentException("Unknown permission type: ${newPermission.permissionType}") }
    }

    when (newPermission.operation) {
      Operation.ADD -> if (!existingPermissions.contains(userId)) existingPermissions.add(userId)
      Operation.REMOVE -> existingPermissions.remove(userId)
      else -> { throw IllegalArgumentException("Unknown operation type: $newPermission.operation") }
    }
  }

  fun hasPermission(userId: String, permissionType: String): Boolean {
    return when (permissionType) {
      PermissionType.READ -> readUsers.contains(userId)
      PermissionType.WRITE -> writeUsers.contains(userId)
      else -> { throw IllegalArgumentException("Unknown permission type: $permissionType") }
    }
  }
}
