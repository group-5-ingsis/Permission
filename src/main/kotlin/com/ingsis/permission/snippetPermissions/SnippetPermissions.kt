package com.ingsis.permission.snippetPermissions

import com.ingsis.permission.permissions.Permission
import com.ingsis.permission.permissions.PermissionOperation
import com.ingsis.permission.permissions.PermissionType
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

  fun applyPermission(userId: String, permission: Permission) {
    val existingPermissions = when (permission.permissionType) {
      PermissionType.READ -> readUsers
      PermissionType.WRITE -> writeUsers
    }

    when (permission.operation) {
      PermissionOperation.ADD -> if (!existingPermissions.contains(userId)) existingPermissions.add(userId)
      PermissionOperation.REMOVE -> existingPermissions.remove(userId)
    }
  }

  fun hasPermission(userId: String, permissionType: PermissionType): Boolean {
    return when (permissionType) {
      PermissionType.READ -> readUsers.contains(userId)
      PermissionType.WRITE -> writeUsers.contains(userId)
    }
  }
}
