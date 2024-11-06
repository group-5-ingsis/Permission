package com.ingsis.permission.permissions

import com.ingsis.permission.user.SnippetUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/v1/permission")
@RestController
class PermissionController(@Autowired private val permissionService: PermissionService) {

  @GetMapping("/jwt/id")
  fun getUserId(@AuthenticationPrincipal jwt: Jwt): String {
    return jwt.subject
  }

  @GetMapping("/read")
  fun getReadableSnippets(@AuthenticationPrincipal jwt: Jwt): List<String> {
    val userId = jwt.subject
    return permissionService.getSnippets(userId, "Read")
  }

  @GetMapping("/write")
  fun getWritableSnippets(@AuthenticationPrincipal jwt: Jwt): List<String> {
    val userId = jwt.subject
    return permissionService.getSnippets(userId, "Write")
  }

  @PostMapping("/update/read/{snippetId}")
  fun updateReadPermissions(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String): SnippetUser {
    val userId = jwt.subject
    return permissionService.updatePermission(userId, snippetId, "Read")
  }

  @PostMapping("/update/write/{snippetId}")
  fun updateWritePermissions(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String): SnippetUser {
    val userId = jwt.subject
    return permissionService.updatePermission(userId, snippetId, "Write")
  }
}
