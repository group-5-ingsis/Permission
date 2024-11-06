package com.ingsis.permission.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository, @Autowired private val authService: AuthService)
