package com.ingsis.permission.permissions

import com.ingsis.permission.user.UserData
import com.ingsis.permission.user.UserDto
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PermissionController(@Autowired private val permissionService: PermissionService) {

  companion object {
    const val CORRELATION_ID_HEADER = "X-Correlation-ID"
    const val NO_CORRELATION_ID = "No-Correlation-ID" // Tag to indicate no correlation ID was received
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

  @GetMapping("/write")
  fun getWritableSnippets(@RequestBody userData: UserData, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get writable snippets for user: ${userData.username}")
    val snippets = permissionService.getSnippets(userData.userId, userData.username, "Write")
    logger.info("Returning writable snippets for user: ${userData.username}, snippets count: ${snippets.size}")
    return snippets
  }

  @GetMapping("/users")
  fun getUsers(request: HttpServletRequest): List<UserDto> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get users list")
    val users = permissionService.getUsers()
    logger.info("Returning users list, count: ${users.size}")
    return users
  }

  @GetMapping("/")
  fun getWritableAndReadableSnippets(@RequestBody userData: UserData, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get readable and writable snippets for user: ${userData.username}")
    val readable = permissionService.getSnippets(userData.userId, userData.username, "Read")
    val writable = permissionService.getSnippets(userData.userId, userData.username, "Write")
    val allSnippets = readable + writable
    logger.info("Returning readable and writable snippets for user: ${userData.username}, total count: ${allSnippets.size}")
    return allSnippets
  }

  @GetMapping("/read")
  fun getReadableSnippets(@RequestBody userData: UserData, request: HttpServletRequest): List<String> {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to get readable snippets for user: ${userData.username}")
    val snippets = permissionService.getSnippets(userData.userId, userData.username, "Read")
    logger.info("Returning readable snippets for user: ${userData.username}, snippets count: ${snippets.size}")
    return snippets
  }

  @PostMapping("/read/{userId}/{snippetId}")
  fun updateReadPermissions(@RequestBody userData: UserData, @PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to update read permissions for user: ${userData.username}, snippetId: $snippetId, targetUserId: $userId")
    permissionService.updatePermission(userId, userData.username, snippetId, "Read")
    logger.info("Updated read permissions for snippetId: $snippetId, targetUserId: $userId")
  }

  @PostMapping("/write/{userId}/{snippetId}")
  fun updateWritePermissions(@RequestBody userData: UserData, @PathVariable snippetId: String, @PathVariable userId: String, request: HttpServletRequest) {
    setCorrelationIdFromHeader(request)
    logger.info("Received request to update write permissions for user: ${userData.username}, snippetId: $snippetId, targetUserId: $userId")
    permissionService.updatePermission(userData.userId, userData.username, snippetId, "Write")
    logger.info("Updated write permissions for snippetId: $snippetId, targetUserId: $userId")
  }
}
