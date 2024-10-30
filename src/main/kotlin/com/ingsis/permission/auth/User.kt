package com.ingsis.permission.auth

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class User(
  @Id
  var auth0id: String,

  @ElementCollection
  var readableSnippets: List<String>,

  @ElementCollection
  var writableSnippets: List<String>
) {
  constructor() : this(
    auth0id = "",
    readableSnippets = emptyList(),
    writableSnippets = emptyList()
  )
}
