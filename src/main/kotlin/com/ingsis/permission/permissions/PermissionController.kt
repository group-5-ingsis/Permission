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
      log("No correlation ID found in the request headers; using default tag.")
    } else {
      log("Correlation ID set to MDC: $correlationId")
    }
  }

  @GetMapping("/{type}/{userId}")
  fun getUserSnippetsOfType(@PathVariable type: String, @PathVariable userId: String, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    log("Received request to get readable snippets for user: $userId")
    val snippets = permissionService.getSnippets(userId, type)
    log("Returning readable snippets for user: $userId, snippets count: ${snippets.size}")
    return snippets
  }

  @PostMapping("/{type}/{operation}/{userId}/{snippetId}")
  fun updatePermissions(@PathVariable type: String, @PathVariable operation: String, @PathVariable userId: String, @PathVariable snippetId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    log("Received request to update read permissions for user: $userId, snippetId: $snippetId")
    val snippetUser = SnippetUser(snippetId, userId)
    val newPermissionChange = PermissionChange(type, operation)
    permissionService.updatePermissions(snippetUser, newPermissionChange)
    log("Updated read permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @GetMapping("/{type}/{snippetId}/{userId}")
  fun hasPermission(@PathVariable type: String, @PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest): Boolean {
    setCorrelationIdFromHeader(request)
    logger.info("Checking if user: $userId has permissions to $type snippet: $snippetId")
    val snippetUser = SnippetUser(snippetId, userId)
    return permissionService.hasPermission(snippetUser, type)
  }

  @DeleteMapping("/{snippetId}")
  fun deleteSnippet(@PathVariable snippetId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to delete snippetId: $snippetId")
    permissionService.deleteSnippet(snippetId)
    logger.info("Successfully deleted snippet with id: $snippetId")
  }

  private fun log(message: String) {
    logger.info(message)
  }
}
