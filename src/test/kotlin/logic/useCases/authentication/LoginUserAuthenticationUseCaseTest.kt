package logic.useCases.authentication

import com.google.common.truth.Truth.assertThat
import fake.createUser
import io.mockk.every
import io.mockk.mockk
import logic.entities.UserRole
import logic.repository.AuthenticationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.MessageDigest

class LoginUserAuthenticationUseCaseTest() {
    private lateinit var repository: AuthenticationRepository
    private lateinit var useCase: LoginUserAuthenticationUseCase

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = LoginUserAuthenticationUseCase(repository)
    }

    @Test
    fun `should return false when user not found`() {
        //Given
        every { repository.loginUser("john", any()) } returns null

        //When
        val result = useCase.execute("john", "pass123")

        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should return false when password does not match`() {
        //Given
        val user = createUser("john", "wronghash", UserRole.MATE)
        every { repository.loginUser("john", any()) } returns user

        //When
        val result = useCase.execute("john", "pass123")

        //Then
        assertThat(result).isFalse()
    }

    @Test
    fun `should login with correct credentials`() {
        //Given
        val user = createUser("admin", hashPassword("123"), UserRole.ADMIN)
        every { repository.loginUser("admin", any()) } returns user

        //When
        val result = useCase.execute("admin", "123")

        //Then
        assertThat(result).isTrue()
    }

    @Test
    fun `should fail login with incorrect credentials`() {
        //Given
        every { repository.loginUser("admin", "wrong") } returns null

        // When
        val result = useCase.execute("admin", "wrong")

        //Then
        assertThat(result).isFalse()
    }

    private fun hashPassword(password: String): String {
        return MessageDigest.getInstance("MD5")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}