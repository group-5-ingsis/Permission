package com.ingsis.permission.user

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
    var id: Long = 0,

    var username: String = "",

    var email: String = "",

    var password: String = ""
) {
    constructor(username: String, email: String, password: String) : this(0, username, email, password)
}
