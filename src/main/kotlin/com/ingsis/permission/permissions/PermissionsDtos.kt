package com.ingsis.permission.permissions

data class Permission(
  val permissionType: PermissionType,
  val operation: PermissionOperation
)

enum class PermissionType {
  WRITE,
  READ
}

enum class PermissionOperation {
  ADD,
  REMOVE
}

enum class DeleteResult {
  FULLY_DELETED,
  PERMISSION_REMOVED,
  NOT_FOUND
}
