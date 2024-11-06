package com.ingsis.permission.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<SnippetUser, Long> {
  fun findByAuth0id(auth0id: String): SnippetUser?
}
