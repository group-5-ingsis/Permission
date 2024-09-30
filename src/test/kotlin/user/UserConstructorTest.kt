package user

import com.ingsis.permission.user.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserConstructorTest {

    @Test
    fun `should create user with specified attributes`() {
        val username = "testUser"
        val email = "test@example.com"
        val password = "securePassword"

        val user = User(0, username, email, password)

        assertEquals(username, user.username)
        assertEquals(email, user.email)
        assertEquals(password, user.password)
    }

    @Test
    fun `should create user with default constructor`() {
        val user = User()

        assertEquals(0, user.id)
        assertEquals("", user.username)
        assertEquals("", user.email)
        assertEquals("", user.password)
    }
}
