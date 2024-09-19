package com.example.Permission.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloWorldController {

  @GetMapping("/hello")
  fun sayHello(): String {
    return "Hello, Kotlin Spring Boot!"
  }
}