package com.example.permission.controller

import org.springframework.web.bind.annotation.*

@RestController("/api")
class HelloWorldController {

    @GetMapping("/hello")
    fun sayHello(): String {
        return "Hello, Kotlin Spring Boot!"
    }

    @PostMapping("/receive-message")
    fun receiveMessage(@RequestBody message: String): String {
        println("Server B received message: $message")
        return "Hello from Permission server!"
    }
}
