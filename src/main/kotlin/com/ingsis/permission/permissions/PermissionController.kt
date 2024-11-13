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

  private val claimsKey = System.getProperty("CLAIMS_KEY")

  @GetMapping("/write")
  fun getWritableSnippets(@AuthenticationPrincipal jwt: Jwt): List<String> {
    val (userId, username) = extractUserInfo(jwt)
    return permissionService.getSnippets(userId, username, "Write")
  }

  @GetMapping("/username")
  fun getUsername(@AuthenticationPrincipal jwt: Jwt): String {
    val (_, username) = extractUserInfo(jwt)
    return username
  }

  @PostMapping("/write/update/{snippetId}")
  fun updateWritePermissions(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String): SnippetUser {
    val (userId, username) = extractUserInfo(jwt)
    return permissionService.updatePermission(userId, username, snippetId, "Write")
  }

  @GetMapping("/read")
  fun getReadableSnippets(@AuthenticationPrincipal jwt: Jwt): List<String> {
    val (userId, username) = extractUserInfo(jwt)
    return permissionService.getSnippets(userId, username, "Read")
  }

  @PostMapping("/read/update/{snippetId}")
  fun updateReadPermissions(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String): SnippetUser {
    val (userId, username) = extractUserInfo(jwt)
    return permissionService.updatePermission(userId, username, snippetId, "Read")
  }

  private fun extractUserInfo(jwt: Jwt): Pair<String, String> {
    val userId = jwt.subject
    val username = jwt.claims["$claimsKey/username"]?.toString() ?: "unknown"
    return Pair(userId, username)
  }
}
