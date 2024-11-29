package com.ingsis.permission.permissions

data class PermissionChange(
  val permissionType: String,
  val operation: String
)

data class SnippetUser(
  val snippetId: String,
  val userId: String
)
