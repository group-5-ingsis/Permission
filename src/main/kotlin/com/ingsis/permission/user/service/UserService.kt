package com.ingsis.permission.user.service

import com.ingsis.permission.user.dtos.UserDto
import com.ingsis.permission.user.entities.User
import com.ingsis.permission.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
  private val userRepository: UserRepository
) {

  fun createUser(userInfo: UserDto): User {
    val user = User(userInfo.username, userInfo.email, userInfo.password)
    return userRepository.save(user)
  }

  fun modifyUser(id: Long, userInfo: UserDto): User? {
    val userExists = userRepository.existsById(id)
    return if (userExists) {
      val existingUser = userRepository.findById(id).get()

      val updatedUser = existingUser.copy(
        username = userInfo.username,
        email = userInfo.email,
        password = userInfo.password
      )

      userRepository.save(updatedUser)
    } else {
      null
    }
  }

  fun deleteUser(id: Long): Boolean {
    val userExists = userRepository.existsById(id)
    return if (userExists) {
      userRepository.deleteById(id)
      true
    } else {
      false
    }
  }
}
