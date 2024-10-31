package com.ingsis.permission.auth

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<SnippetUser, Long>
