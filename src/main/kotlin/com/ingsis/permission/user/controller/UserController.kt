 
package com.ingsis.permission.user.controller

import com.ingsis.permission.user.dtos.UserDto
import com.ingsis.permission.user.entities.User
import com.ingsis.permission.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController @Autowired constructor(
  private val userService: UserService
) {

  @PostMapping("/create")
  fun createUser(@RequestBody user: UserDto): ResponseEntity<User> {
    val createdUser = userService.createUser(user)
    return ResponseEntity(createdUser, HttpStatus.CREATED)
  }

  @PutMapping("/{id}")
  fun modifyUser(@PathVariable id: String, @RequestBody user: UserDto): ResponseEntity<User> {
    val updatedUser = userService.modifyUser(id, user)
    return if (updatedUser != null) {
      ResponseEntity(updatedUser, HttpStatus.OK)
    } else {
      ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }

  @DeleteMapping("/{id}")
  fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
    val userDeleted = userService.deleteUser(id)
    return if (userDeleted) {
      ResponseEntity(HttpStatus.NO_CONTENT)
    } else {
      ResponseEntity(HttpStatus.NOT_FOUND)
    }
  }
}
