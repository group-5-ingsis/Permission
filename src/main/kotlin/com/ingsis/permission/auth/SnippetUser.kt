package com.ingsis.permission.auth

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class SnippetUser(
  @Id
  var auth0id: String,

  var username: String,

  @ElementCollection
  var readableSnippets: List<String>,

  @ElementCollection
  var writableSnippets: List<String>
) {
  constructor() : this(
    auth0id = "",
    username = "",
    readableSnippets = emptyList(),
    writableSnippets = emptyList()
  )
}
