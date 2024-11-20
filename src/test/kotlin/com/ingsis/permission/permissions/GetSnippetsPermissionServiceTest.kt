package com.ingsis.permission.permissions
import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class GetSnippetsPermissionServiceTest {

  @Mock
  private lateinit var snippetPermissionsRepository: SnippetPermissionsRepository

  @InjectMocks
  private lateinit var permissionService: PermissionService

  @Test
  fun `getSnippets should return readable snippets`() {
    val userId = "user1"
    val snippetPermissions = listOf(
      SnippetPermissions(id = "snippet1", readUsers = mutableListOf(userId)),
      SnippetPermissions(id = "snippet2", readUsers = mutableListOf())
    )

    `when`(snippetPermissionsRepository.findAll()).thenReturn(snippetPermissions)

    val result = permissionService.getSnippets(userId, "read")

    assertEquals(listOf("snippet1"), result)
  }

  @Test
  fun `getSnippets should return writable snippets`() {
    val userId = "user1"
    val snippetPermissions = listOf(
      SnippetPermissions(id = "snippet1", writeUsers = mutableListOf(userId)),
      SnippetPermissions(id = "snippet2", writeUsers = mutableListOf())
    )

    `when`(snippetPermissionsRepository.findAll()).thenReturn(snippetPermissions)

    val result = permissionService.getSnippets(userId, "write")

    assertEquals(listOf("snippet1"), result)
  }

  @Test
  fun `getSnippets should return empty list for unknown type`() {
    val userId = "user1"

    val result = permissionService.getSnippets(userId, "unknown")

    assertEquals(emptyList<String>(), result)
  }
}
