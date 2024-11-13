package com.ingsis.permission.user

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

fun SnippetUser.toUserDto(): UserDto {
  return UserDto(
    id = this.auth0id,
    name = this.username
  )
}
