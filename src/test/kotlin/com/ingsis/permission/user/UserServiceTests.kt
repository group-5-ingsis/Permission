package com.ingsis.permission.user

import com.ingsis.permission.user.dtos.UserDto
import com.ingsis.permission.user.entities.User
import com.ingsis.permission.user.repository.UserRepository
import com.ingsis.permission.user.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.Optional

class UserServiceTests {

  private lateinit var userRepository: UserRepository
  private lateinit var userService: UserService

  @BeforeEach
  fun setUp() {
    userRepository = mock(UserRepository::class.java)
    userService = UserService(userRepository)
  }

  @Test
  fun `createUser should save user and return it`() {
    val userDto = UserDto("testUser", "test@example.com", "password")
    val expectedUser = User("testUser", "test@example.com", "password")

    `when`(userRepository.save(any(User::class.java))).thenReturn(expectedUser)

    val createdUser = userService.createUser(userDto)

    assertNotNull(createdUser)
    assertEquals(expectedUser.username, createdUser.username)
    assertEquals(expectedUser.email, createdUser.email)
    assertEquals(expectedUser.password, createdUser.password)
  }

  @Test
  fun `modifyUser should update user if exists`() {
    val id = 1L
    val userDto = UserDto("updatedUser", "updated@example.com", "newPassword")
    val existingUser = User("existingUser", "existing@example.com", "oldPassword")
    val updatedUser = existingUser.copy(
      username = userDto.username,
      email = userDto.email,
      password = userDto.password
    )

    `when`(userRepository.existsById(id)).thenReturn(true)
    `when`(userRepository.findById(id)).thenReturn(Optional.of(existingUser))
    `when`(userRepository.save(updatedUser)).thenReturn(updatedUser)

    val result = userService.modifyUser(id, userDto)

    assertNotNull(result)
    assertEquals(updatedUser.username, result?.username)
    assertEquals(updatedUser.email, result?.email)
    assertEquals(updatedUser.password, result?.password)
  }

  @Test
  fun `modifyUser should return null if user does not exist`() {
    val id = 1L
    val userDto = UserDto("testUser", "test@example.com", "password")

    `when`(userRepository.existsById(id)).thenReturn(false)

    val result = userService.modifyUser(id, userDto)

    assertNull(result)
  }

  @Test
  fun `deleteUser should return true if user exists and is deleted`() {
    val id = 1L

    `when`(userRepository.existsById(id)).thenReturn(true)

    val result = userService.deleteUser(id)

    assertTrue(result)
  }

  @Test
  fun `deleteUser should return false if user does not exist`() {
    val id = 1L

    `when`(userRepository.existsById(id)).thenReturn(false)

    val result = userService.deleteUser(id)

    assertFalse(result)
  }
}
