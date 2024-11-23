package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetUser
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PermissionController(@Autowired private val permissionService: PermissionService) {

  companion object {
    const val CORRELATION_ID_HEADER = "X-Correlation-ID"
    const val NO_CORRELATION_ID = "No-Correlation-ID"
  }

  private val logger = LoggerFactory.getLogger(PermissionController::class.java)

  private fun setCorrelationIdFromHeader(request: HttpServletRequest) {
    val correlationId = request.getHeader(CORRELATION_ID_HEADER) ?: NO_CORRELATION_ID
    MDC.put(CORRELATION_ID_HEADER, correlationId)
    if (correlationId == NO_CORRELATION_ID) {
      log("No correlation ID found in the request headers; using default tag.")
    } else {
      log("Correlation ID set to MDC: $correlationId")
    }
  }

  @GetMapping("/write/{userId}")
  fun getWritableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    log("Received request to get writable snippets for user: $userId")
    val snippets = permissionService.getSnippets(userId, PermissionType.WRITE)
    log("Returning writable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/read/{userId}")
  fun getReadableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    log("Received request to get readable snippets for user: $userId")
    val snippets = permissionService.getSnippets(userId, PermissionType.READ)
    log("Returning readable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/{userId}")
  fun getWritableAndReadableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    log("Received request to get readable and writable snippets for user: $userId")
    val readable = permissionService.getSnippets(userId, PermissionType.READ)
    val writable = permissionService.getSnippets(userId, PermissionType.WRITE)
    val allSnippets = readable + writable
    log("Returning readable and writable snippets for user: $userId, total count: ${allSnippets.size}")
    return allSnippets
  }

  @PostMapping("/read/{userId}/{snippetId}")
  fun updateReadPermissions(@PathVariable userId: String, @PathVariable snippetId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    log("Received request to update read permissions for user: $userId, snippetId: $snippetId")
    val snippetUser = SnippetUser(snippetId, userId)
    val newPermission = Permission(PermissionType.READ, PermissionOperation.ADD)
    permissionService.updatePermissions(snippetUser, newPermission)
    log("Updated read permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @PostMapping("/write/{userId}/{snippetId}")
  fun updateWritePermissions(@PathVariable userId: String, @PathVariable snippetId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    log("Received request to update write permissions for user: $userId, snippetId: $snippetId, targetUserId: $userId")
    val snippetUser = SnippetUser(snippetId, userId)
    val permission = Permission(PermissionType.WRITE, PermissionOperation.ADD)
    permissionService.updatePermissions(snippetUser, permission)
    log("Updated write permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @DeleteMapping("/{userId}/{snippetId}")
  fun deleteSnippet(@PathVariable userId: String, @PathVariable snippetId: String, request: HttpServletRequest): DeleteResult {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to delete snippetId: $snippetId")
    val snippetUser = SnippetUser(snippetId, userId)
    return permissionService.deleteSnippet(snippetUser)
  }

  private fun log(message: String) {
    logger.info(message)
  }
}
