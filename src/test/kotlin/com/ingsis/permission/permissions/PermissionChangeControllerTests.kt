package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.Operation.ADD
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.Operation.REMOVE
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.PermissionType.WRITE
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@ExtendWith(MockitoExtension::class)
class PermissionChangeControllerTests {

  @Mock
  private lateinit var permissionService: PermissionService

  @InjectMocks
  private lateinit var permissionController: PermissionController

  @Mock
  private lateinit var request: HttpServletRequest

  @Test
  fun `updateWritePermissions should update write permissions`() {
    val userId = "user1"
    val snippetId = "snippet1"

    `when`(request.getHeader(PermissionController.CORRELATION_ID_HEADER)).thenReturn("test-correlation-id")
    RequestContextHolder.setRequestAttributes(ServletRequestAttributes(request))

    permissionController.updatePermissions(WRITE, ADD, userId, snippetId, request)

    val snippetUser = SnippetUser(userId = userId, snippetId = snippetId)

    val permissionChange = PermissionChange(WRITE, ADD)

    verify(permissionService).updatePermissions(snippetUser, permissionChange)
  }

  @Test
  fun `removePermissions should remove read permissions`() {
    val userId = "user1"
    val snippetId = "snippet1"

    `when`(request.getHeader(PermissionController.CORRELATION_ID_HEADER)).thenReturn("test-correlation-id")
    RequestContextHolder.setRequestAttributes(ServletRequestAttributes(request))

    permissionController.updatePermissions(WRITE, REMOVE, userId, snippetId, request)

    val snippetUser = SnippetUser(userId = userId, snippetId = snippetId)

    val permissionChange = PermissionChange(permissionType = WRITE, REMOVE)

    verify(permissionService).updatePermissions(snippetUser, permissionChange)
  }
}
