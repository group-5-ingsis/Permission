package com.ingsis.permission.user

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest(properties = ["spring.datasource.url=jdbc:h2:mem:testdb"])
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
        val userDto = UserDto(
            "john_doe",
            "john@example.com",
            "password123"
        )

        val response: ResponseEntity<User> = userController.createUser(userDto)

        assert(response.statusCode.is2xxSuccessful)
        val createdUser = response.body
        assert(createdUser != null)
        assert(createdUser!!.id > 0)
        assert(createdUser.username == "john_doe")
        assert(createdUser.email == "john@example.com")
    }

    @Test
    fun `test modifyUser endpoint - success`() {
        val user = userRepository.save(User(0, "john_doe", "john@example.com", "password123"))

        val updatedUserDto = UserDto(
            username = "john_doe_updated",
            email = "john_updated@example.com",
            password = "newpassword123"
        )

        val response: ResponseEntity<User> = userController.modifyUser(user.id, updatedUserDto)

        assert(response.statusCode == HttpStatus.OK)

        val updatedUser = response.body

        assert(updatedUser != null)

        if (updatedUser != null) {
            assert(updatedUser.id == user.id)
        }
        if (updatedUser != null) {
            assert(updatedUser.username == "john_doe_updated")
        }
        if (updatedUser != null) {
            assert(updatedUser.email == "john_updated@example.com")
        }
    }

    @Test
    fun `test deleteUser endpoint - success`() {
        val user = userRepository.save(User(0, "john_doe", "john@example.com", "password123"))

        val response: ResponseEntity<Void> = userController.deleteUser(user.id)

        assert(response.statusCode == HttpStatus.NO_CONTENT)

        val userExists = userRepository.existsById(user.id)
        assert(!userExists)
    }

    @Test
    fun `test deleteUser endpoint - user not found`() {
        val response: ResponseEntity<Void> = userController.deleteUser(999)

        assert(response.statusCode == HttpStatus.NOT_FOUND)
    }
}
