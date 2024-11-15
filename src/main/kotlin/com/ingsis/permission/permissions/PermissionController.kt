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
    val snippets = permissionService.getSnippets(userId, "Write")
    logger.info("Returning writable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/read/{userId}")
  fun getReadableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get readable snippets for user: $userId")
    val snippets = permissionService.getSnippets(userId, "Read")
    logger.info("Returning readable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/{userId}")
  fun getWritableAndReadableSnippets(@PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get readable and writable snippets for user: $userId")
    val readable = permissionService.getSnippets(userId, "read")
    val writable = permissionService.getSnippets(userId, "write")
    val allSnippets = readable + writable
    logger.info("Returning readable and writable snippets for user: $userId, total count: ${allSnippets.size}")
    return allSnippets
  }

  @PostMapping("/read/{userId}/{snippetId}")
  fun updateReadPermissions(@PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to update read permissions for user: $userId, snippetId: $snippetId")
    permissionService.updatePermission(userId, snippetId, "Read")
    logger.info("Updated read permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @PostMapping("/write/{userId}/{snippetId}")
  fun updateWritePermissions(@PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to update write permissions for user: $userId, snippetId: $snippetId, targetUserId: $userId")
    permissionService.updatePermission(userId, snippetId, "Write")
    logger.info("Updated write permissions for snippetId: $snippetId, targetUserId: $userId")
  }
}
