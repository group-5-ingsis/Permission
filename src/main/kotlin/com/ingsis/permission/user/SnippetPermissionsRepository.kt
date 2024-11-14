package com.ingsis.permission.user

import org.springframework.data.jpa.repository.JpaRepository

interface SnippetPermissionsRepository : JpaRepository<SnippetPermissions, String>
