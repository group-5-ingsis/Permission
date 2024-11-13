package com.ingsis.permission.permissions

import com.ingsis.permission.user.UserData
import com.ingsis.permission.user.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PermissionController(@Autowired private val permissionService: PermissionService) {

  @GetMapping("/write")
  fun getWritableSnippets(@RequestBody userData: UserData): List<String> {
    return permissionService.getSnippets(userData.userId, userData.username, "Write")
  }

  @GetMapping("/users")
  fun getUsers(): List<UserDto> {
    return permissionService.getUsers()
  }

  @GetMapping("/")
  fun getWritableAndReadableSnippets(@RequestBody userData: UserData): List<String> {
    val readable = permissionService.getSnippets(userData.userId, userData.username, "Read")
    val writable = permissionService.getSnippets(userData.userId, userData.username, "Write")
    return readable + writable
  }

  @GetMapping("/read")
  fun getReadableSnippets(@RequestBody userData: UserData): List<String> {
    return permissionService.getSnippets(userData.userId, userData.username, "Read")
  }

  // Revisar estos 2 methods
  @PostMapping("/read/{userId}/{snippetId}")
  fun updateReadPermissions(@RequestBody userData: UserData, @PathVariable snippetId: String, @PathVariable userId: String) {
    return permissionService.updatePermission(userId, userData.username, snippetId, "Read")
  }

  @PostMapping("/write/{userId}/{snippetId}")
  fun updateWritePermissions(@RequestBody userData: UserData, @PathVariable snippetId: String, @PathVariable userId: String) {
    return permissionService.updatePermission(userData.userId, userData.username, snippetId, "Write")
  }
}
