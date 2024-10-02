package com.ingsis.permission.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(UserController::class)
class UserControllerTests @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    @MockBean
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        reset(userService)
    }

    @Test
    fun `createUser should return 201 and the created user`() {
        val userDto = UserDto("testUser", "test@example.com", "password")
        val createdUser = User("testUser", "test@example.com", "password")

        `when`(userService.createUser(userDto)).thenReturn(createdUser)

        mockMvc.perform(
            post("/api/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value(createdUser.username))
            .andExpect(jsonPath("$.email").value(createdUser.email))
    }

    @Test
    fun `modifyUser should return 200 and the updated user`() {
        val id = 1L
        val userDto = UserDto("updatedUser", "updated@example.com", "newPassword")
        val existingUser = User("existingUser", "existing@example.com", "oldPassword")
        val updatedUser = existingUser.copy(
            username = userDto.username,
            email = userDto.email,
            password = userDto.password
        )

        `when`(userService.modifyUser(id, userDto)).thenReturn(updatedUser)

        mockMvc.perform(
            put("/api/users/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.username").value(updatedUser.username))
            .andExpect(jsonPath("$.email").value(updatedUser.email))
    }

    @Test
    fun `modifyUser should return 404 if user does not exist`() {
        val id = 1L
        val userDto = UserDto("updatedUser", "updated@example.com", "newPassword")

        `when`(userService.modifyUser(id, userDto)).thenReturn(null)

        mockMvc.perform(
            put("/api/users/$id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `deleteUser should return 204 if user is deleted`() {
        val id = 1L

        `when`(userService.deleteUser(id)).thenReturn(true)

        mockMvc.perform(delete("/api/users/$id"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `deleteUser should return 404 if user does not exist`() {
        val id = 1L

        `when`(userService.deleteUser(id)).thenReturn(false)

        mockMvc.perform(delete("/api/users/$id"))
            .andExpect(status().isNotFound)
    }
}
