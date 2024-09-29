package com.ingsis.permission.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(unique = true, nullable = false)
    val username: String,

    @Column(unique = true, nullable = false)
    val email: String,

    val password: String

)
