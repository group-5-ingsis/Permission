package com.ingsis.permission.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
    }

    @Test
    fun `test createUser endpoint - success`() {
        val userDto = UserDto(
            username = "john_doe",
            email = "john@example.com",
            password = "password123"
        )

        mockMvc.perform(
            post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.username").value("john_doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"))
            .andDo(print())
    }

    @Test
    fun `test modifyUser endpoint - success`() {
        val user = userRepository.save(User(0, "john_doe", "john@example.com", "password123"))

        val updatedUserDto = UserDto(
            username = "john_doe_updated",
            email = "john_updated@example.com",
            password = "newpassword123"
        )

        mockMvc.perform(
            put("/api/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUserDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id))
            .andExpect(jsonPath("$.username").value("john_doe_updated"))
            .andExpect(jsonPath("$.email").value("john_updated@example.com"))
            .andDo(print())
    }

    @Test
    fun `test deleteUser endpoint - success`() {
        val user = userRepository.save(User(0, "john_doe", "john@example.com", "password123"))

        mockMvc.perform(
            delete("/api/users/${user.id}")
        )
            .andExpect(status().isNoContent)
            .andDo(print())
    }

    @Test
    fun `test deleteUser endpoint - user not found`() {
        mockMvc.perform(
            delete("/api/users/999")
        )
            .andExpect(status().isNotFound)
            .andDo(print())
    }
}
