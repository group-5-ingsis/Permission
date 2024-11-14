package com.ingsis.permission.user

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class SnippetPermissions(
  @Id
  var id: String,

  @ElementCollection
  var readUsers: MutableList<String> = mutableListOf(),

  @ElementCollection
  var writeUsers: MutableList<String> = mutableListOf()
) {
  constructor() : this(id = "")

  fun addReadPermission(editorId: String) {
    if (!readUsers.contains(editorId)) {
      readUsers.add(editorId)
    }
  }

  fun addWritePermission(editorId: String) {
    if (!writeUsers.contains(editorId)) {
      writeUsers.add(editorId)
    }
  }
}
