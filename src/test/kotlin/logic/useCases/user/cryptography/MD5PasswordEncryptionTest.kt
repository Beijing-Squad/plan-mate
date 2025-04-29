package logic.useCases.user.cryptography

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MD5PasswordEncryptionTest {

    private lateinit var mD5PasswordEncryption: PasswordEncryption

    @BeforeEach
    fun setUp() {
        mD5PasswordEncryption = MD5PasswordEncryption()
    }

    @Test
    fun `should encryption password when call md5 encryption`() {
        // Given
        val password = "12345678"
        val expectedHash = "25d55ad283aa400af464c76d713c07ad"

        // When
        val hashPassword = mD5PasswordEncryption.encryptionPassword(password)

        // Then
        assertThat(hashPassword).isEqualTo(expectedHash)

    }


}