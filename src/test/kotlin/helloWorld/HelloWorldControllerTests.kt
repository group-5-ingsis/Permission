package helloWorld

import com.ingsis.permission.helloWorld.HelloWorldController
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HelloWorldControllerTests {

    private val controller = HelloWorldController()

    @Test
    fun `should return Hello, Kotlin Spring Boot!`() {
        val response = controller.sayHello()
        assertEquals("Hello, Kotlin Spring Boot!", response)
    }

    @Test
    fun `should return Hello from Permission server`() {
        val response = controller.receiveMessage()
        assertEquals("Hello from Permission server!", response)
    }
}
