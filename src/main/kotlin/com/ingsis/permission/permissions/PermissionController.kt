package com.ingsis.permission.permissions

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
    const val WRITE_PERMISSION = "write"
    const val READ_PERMISSION = "read"
  }

  private val logger = LoggerFactory.getLogger(PermissionController::class.java)

  private fun setCorrelationIdFromHeader(request: HttpServletRequest) {
    val correlationId = request.getHeader(CORRELATION_ID_HEADER) ?: NO_CORRELATION_ID
    MDC.put(CORRELATION_ID_HEADER, correlationId)
    if (correlationId == NO_CORRELATION_ID) {
      logger.warn("No correlation ID found in the request headers; using default tag.")
    } else {
      logger.info("Correlation ID set to MDC: $correlationId")
    }
  }

  @GetMapping("/write/{userId}")
  fun getWritableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get writable snippets for user: $userId")
    val snippets = permissionService.getSnippets(userId, WRITE_PERMISSION)
    logger.info("Returning writable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/read/{userId}")
  fun getReadableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get readable snippets for user: $userId")
    val snippets = permissionService.getSnippets(userId, READ_PERMISSION)
    logger.info("Returning readable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/{userId}")
  fun getWritableAndReadableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get readable and writable snippets for user: $userId")
    val readable = permissionService.getSnippets(userId, READ_PERMISSION)
    val writable = permissionService.getSnippets(userId, WRITE_PERMISSION)
    val allSnippets = readable + writable
    logger.info("Returning readable and writable snippets for user: $userId, total count: ${allSnippets.size}")
    return allSnippets
  }

  @PostMapping("/read/{userId}/{snippetId}")
  fun updateReadPermissions(@PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to update read permissions for user: $userId, snippetId: $snippetId")
    permissionService.updatePermission(userId, snippetId, READ_PERMISSION)
    logger.info("Updated read permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @PostMapping("/write/{userId}/{snippetId}")
  fun updateWritePermissions(@PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to update write permissions for user: $userId, snippetId: $snippetId, targetUserId: $userId")
    permissionService.updatePermission(userId, snippetId, WRITE_PERMISSION)
    logger.info("Updated write permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @DeleteMapping("/{snippetId}")
  fun deleteSnippet(@PathVariable snippetId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to delete snippetId: $snippetId")
    permissionService.deleteSnippet(snippetId)
    logger.info("Deleted snippet with id: $snippetId")
  }

  @DeleteMapping("/read/{snippetId}/{userId}")
  fun removePermissions(@PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to remove permissions for $userId for snippetId: $snippetId")
    permissionService.removePermissions(snippetId, userId, READ_PERMISSION)
    logger.info("Removed permissions for $userId for snippet with id: $snippetId")
  }
}
