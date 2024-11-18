package com.ingsis.permission.snippetPermissions

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
