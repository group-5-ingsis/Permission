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
    val (userId, email) = extractUserInfo(jwt)
    return permissionService.getSnippets(userId, email, "Write")
  }

  @GetMapping("/email")
  fun getEmail(@AuthenticationPrincipal jwt: Jwt): String {
    val (_, email) = extractUserInfo(jwt)
    return email
  }

  @PostMapping("/write/update/{snippetId}")
  fun updateWritePermissions(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String): SnippetUser {
    val (userId, email) = extractUserInfo(jwt)
    return permissionService.updatePermission(userId, email, snippetId, "Write")
  }

  @GetMapping("/read")
  fun getReadableSnippets(@AuthenticationPrincipal jwt: Jwt): List<String> {
    val (userId, email) = extractUserInfo(jwt)
    return permissionService.getSnippets(userId, email, "Read")
  }

  @PostMapping("/read/update/{snippetId}")
  fun updateReadPermissions(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String): SnippetUser {
    val (userId, email) = extractUserInfo(jwt)
    return permissionService.updatePermission(userId, email, snippetId, "Read")
  }

  private fun extractUserInfo(jwt: Jwt): Pair<String, String> {
    val userId = jwt.subject
    val email = jwt.claims["$claimsKey/email"]?.toString() ?: "unknown"
    return Pair(userId, email)
  }
}
