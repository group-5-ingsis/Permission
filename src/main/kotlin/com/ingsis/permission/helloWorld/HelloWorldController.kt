package com.ingsis.permission.helloWorld

import org.springframework.web.bind.annotation.*

@RestController("/api")
class HelloWorldController {

    @GetMapping("/hello")
    fun sayHello(): String {
        return "Hello, Kotlin Spring Boot!"
    }

    @PostMapping("/receive-message")
    fun receiveMessage(): String {
        return "Hello from Permission server!"
    }
}
