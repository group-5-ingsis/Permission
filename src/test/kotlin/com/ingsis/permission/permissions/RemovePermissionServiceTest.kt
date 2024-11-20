package com.ingsis.permission.permissions

import com.ingsis.permission.permissions.PermissionController.Companion.READ_PERMISSION
import com.ingsis.permission.permissions.PermissionController.Companion.WRITE_PERMISSION

import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class RemovePermissionServiceTest {

  @Mock
  private lateinit var snippetPermissionsRepository: SnippetPermissionsRepository

  @InjectMocks
  private lateinit var permissionService: PermissionService

  @Test
  fun `removePermissions should remove read permission`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId, readUsers = mutableListOf(userId), writeUsers = mutableListOf())

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    permissionService.removePermissions(snippetId, userId, READ_PERMISSION)

    val captor = ArgumentCaptor.forClass(SnippetPermissions::class.java)
    verify(snippetPermissionsRepository).save(captor.capture())
    assert(!captor.value.readUsers.contains(userId))
  }

  @Test
  fun `removePermissions should remove write permission`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId, readUsers = mutableListOf(), writeUsers = mutableListOf(userId))

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    permissionService.removePermissions(snippetId, userId, WRITE_PERMISSION)

    val captor = ArgumentCaptor.forClass(SnippetPermissions::class.java)
    verify(snippetPermissionsRepository).save(captor.capture())
    assert(!captor.value.writeUsers.contains(userId))
  }

  @Test
  fun `removePermissions should throw exception for unknown permission type`() {
    val snippetId = "snippet1"
    val userId = "user1"
    val snippetPermissions = SnippetPermissions(id = snippetId, readUsers = mutableListOf(), writeUsers = mutableListOf())

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.of(snippetPermissions))

    assertThrows(IllegalArgumentException::class.java) {
      permissionService.removePermissions(snippetId, userId, "unknown")
    }
  }

  @Test
  fun `removePermissions should throw exception if snippet not found`() {
    val snippetId = "snippet1"
    val userId = "user1"

    `when`(snippetPermissionsRepository.findById(snippetId)).thenReturn(java.util.Optional.empty())

    assertThrows(IllegalArgumentException::class.java) {
      permissionService.removePermissions(snippetId, userId, READ_PERMISSION)
    }
  }
}
