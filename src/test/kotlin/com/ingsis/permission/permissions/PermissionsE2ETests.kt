package com.ingsis.permission.permissions

import com.ingsis.permission.snippetPermissions.SnippetPermissions
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.Operation.ADD
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.PermissionType.READ
import com.ingsis.permission.snippetPermissions.SnippetPermissions.Companion.PermissionType.WRITE
import com.ingsis.permission.snippetPermissions.SnippetPermissionsRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class PermissionsE2ETests @Autowired constructor(
  private val client: WebTestClient,
  private val snippetPermissionsRepository: SnippetPermissionsRepository
) {

  @BeforeEach
  fun setup() {
    val snippet1 = SnippetPermissions("snippet1")
    snippet1.updatePermission("user1", PermissionChange(permissionType = READ, ADD))
    snippet1.updatePermission("user1", PermissionChange(permissionType = WRITE, ADD))
    snippetPermissionsRepository.save(snippet1)

    val snippet2 = SnippetPermissions("snippet2")
    snippet1.updatePermission("user1", PermissionChange(permissionType = READ, ADD))
    snippet1.updatePermission("user1", PermissionChange(permissionType = READ, ADD))
    snippet1.updatePermission("user1", PermissionChange(permissionType = WRITE, ADD))
    snippetPermissionsRepository.save(snippet2)

    snippetPermissionsRepository.findAll().forEach {
      it.readUsers.size
      it.writeUsers.size
    }
  }

  @AfterEach
  fun tearDown() {
    snippetPermissionsRepository.deleteAll()
  }

  @Test
  fun `should return writable snippets for a user`() {
    val response = client.get()
      .uri("/write/user1")
      .header("X-Correlation-ID", "test-correlation-id")
      .exchange()
      .expectStatus().isOk

    val snippets = response.expectBodyList(String::class.java)

    snippets.hasSize(1)
  }

  @Test
  fun `should update write permissions for user`() {
    val response = client.post()
      .uri("/read/add/user1/snippet1")
      .header("X-Correlation-ID", "test-correlation-id")
      .exchange()
      .expectStatus().isOk

    response.expectBodyList(Void::class.java)
  }

  @Test
  fun `should return readable snippets for a user`() {
    val response = client.get()
      .uri("/write/user1")
      .header("X-Correlation-ID", "test-correlation-id")
      .exchange()
      .expectStatus().isOk

    val snippets = response.expectBodyList(String::class.java)

    snippets.hasSize(1)
  }

  @Test
  fun `should return both readable and writable snippets for a user`() {
    val response = client.get()
      .uri("/user1")
      .header("X-Correlation-ID", "test-correlation-id")
      .exchange()
      .expectStatus().isOk

    val snippets = response.expectBodyList(String::class.java)
    snippets.hasSize(1)
  }
}
