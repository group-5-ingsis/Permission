package com.ingsis.permission.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {

    @BeforeEach
    fun resetDatabase() {
        userRepository.deleteAll()
    }

    @Autowired
    private lateinit var userController: UserController

    @Autowired
    private lateinit var userRepository: UserRepository

    private var createdUserId: Long? = null

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
        createdUserId = createdUser.id
    }

    @Test
    fun `test modifyUser endpoint - success`() {
        val userDto = UserDto("john_doe", "john@example.com", "password123")
        val createdResponse: ResponseEntity<User> = userController.createUser(userDto)
        val createdUser = createdResponse.body
        assert(createdUser != null)
        if (createdUser != null) {
            createdUserId = createdUser.id
        }

        val updatedUserDto = UserDto("john_updated", "john_updated@example.com", "newpassword")
        val updateResponse: ResponseEntity<User> = userController.modifyUser(createdUserId!!, updatedUserDto)

        assert(updateResponse.statusCode == HttpStatus.OK)
        val updatedUser = updateResponse.body

        if (updatedUser != null) {
            assert(updatedUser.username == "john_updated")
            assert(updatedUser.email == "john_updated@example.com")
        }
    }

    @Test
    fun `test deleteUser endpoint - success`() {
        val userDto = UserDto("john_doe", "john@example.com", "password123")
        val createdResponse: ResponseEntity<User> = userController.createUser(userDto)
        val createdUser = createdResponse.body
        assert(createdUser != null)

        if (createdUser != null) {
            createdUserId = createdUser.id
        }

        val deleteResponse: ResponseEntity<Void> = userController.deleteUser(createdUserId!!)

        assert(deleteResponse.statusCode == HttpStatus.NO_CONTENT)
        assert(!userRepository.existsById(createdUserId!!))
    }

    @Test
    fun `test deleteUser endpoint - user not found`() {
        val response: ResponseEntity<Void> = userController.deleteUser(99999)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }
}
