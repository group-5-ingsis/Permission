package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class PermissionServiceTestUpdatePermission {

  @Mock
  private lateinit var snippetPermissionsRepository: SnippetPermissionsRepository

  @InjectMocks
  private lateinit var permissionService: PermissionService

  @Test
  fun `updatePermission should add read permission`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId)

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    permissionService.updatePermission(userId, snippetId, "read")

    verify(snippetPermissionsRepository).save(snippetPermissions)
    assertTrue(snippetPermissions.readUsers.contains(userId))
  }

  @Test
  fun `updatePermission should add write permission`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId)

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    permissionService.updatePermission(userId, snippetId, "write")

    verify(snippetPermissionsRepository).save(snippetPermissions)
    assertTrue(snippetPermissions.writeUsers.contains(userId))
  }

  @Test
  fun `updatePermission should throw IllegalArgumentException for unknown type`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId)

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    val exception = assertThrows<IllegalArgumentException> {
      permissionService.updatePermission(userId, snippetId, "unknown")
    }

    assertTrue(exception.message!!.contains("Unknown permission type: unknown"))
  }
}
