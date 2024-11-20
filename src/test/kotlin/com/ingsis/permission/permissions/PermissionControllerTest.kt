package com.ingsis.permission.permissions

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.mockito.Mockito.*

@ExtendWith(MockitoExtension::class)
class PermissionControllerTest {

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

    permissionController.updateWritePermissions(snippetId, userId, request)

    verify(permissionService).updatePermission(userId, snippetId, PermissionController.WRITE_PERMISSION)
  }

  @Test
  fun `removePermissions should remove read permissions`() {
    val userId = "user1"
    val snippetId = "snippet1"

    `when`(request.getHeader(PermissionController.CORRELATION_ID_HEADER)).thenReturn("test-correlation-id")
    RequestContextHolder.setRequestAttributes(ServletRequestAttributes(request))

    permissionController.removePermissions(snippetId, userId, request)

    verify(permissionService).removePermissions(snippetId, userId, PermissionController.READ_PERMISSION)
  }
}
