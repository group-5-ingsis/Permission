package com.ingsis.permission.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class UserControllerTest {

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `test createUser endpoint - success`() {
        val userDto = UserDto("john_doe", "john@example.com", "password123")

        val response: ResponseEntity<User> = userController.createUser(userDto)

        assert(response.statusCode.is2xxSuccessful)
        val createdUser = response.body
        assert(createdUser != null)
        assert(createdUser!!.id > 0)
        assert(createdUser.username == "john_doe")
        assert(createdUser.email == "john@example.com")
    }
}
