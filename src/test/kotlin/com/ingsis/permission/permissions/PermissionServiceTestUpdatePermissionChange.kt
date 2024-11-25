package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.Operation.ADD
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.PermissionType.READ
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.PermissionType.WRITE
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.assertTrue

@ExtendWith(MockitoExtension::class)
class PermissionServiceTestUpdatePermissionChange {

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

    val snippetUser = SnippetUser(userId = userId, snippetId = snippetId)
    val permissionChange = PermissionChange(READ, ADD)

    permissionService.updatePermissions(snippetUser, permissionChange)

    verify(snippetPermissionsRepository).save(snippetPermissions)
    assertTrue(snippetPermissions.readUsers.contains(userId))
  }

  @Test
  fun `updatePermission should add write permission`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId)

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    val snippetUser = SnippetUser(userId = userId, snippetId = snippetId)
    val permissionChange = PermissionChange(permissionType = WRITE, ADD)

    permissionService.updatePermissions(snippetUser, permissionChange)

    verify(snippetPermissionsRepository).save(snippetPermissions)
    assertTrue(snippetPermissions.writeUsers.contains(userId))
  }
}
