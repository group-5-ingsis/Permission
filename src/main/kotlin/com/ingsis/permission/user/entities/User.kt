package com.ingsis.permission.user.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: String = "",

  var username: String = "",

  var email: String = "",

  var password: String = ""
)
