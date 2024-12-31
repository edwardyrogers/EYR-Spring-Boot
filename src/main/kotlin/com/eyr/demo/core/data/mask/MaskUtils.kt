package com.eyr.demo.core.data.mask

import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object MaskUtils {

    private const val ALGORITHM = "AES"

    // Generate a secret key (this should ideally be done once and shared securely)
    fun generateKey(): String = run {
        // Format to only include day, month, and year
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = dateFormat.format(Date())

        // Generate a UUID based on the current date
        val uuid = UUID.nameUUIDFromBytes(dateString.toByteArray())

        // Combine the date with a secret salt for added security
        val input = "PREFIX-$dateString-SUFFIX-$uuid"

        // Use SHA-256 to hash the input
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())

        // Use only the first 16 bytes (128 bits) for AES key
        val truncatedKey = hashBytes.copyOf(32)

        // Encode the key in Base64 format for easy transmission/storage
        return@run Base64.getEncoder().encodeToString(truncatedKey)
    }

    // Encrypt the data
    private fun encrypt(data: String, base64Key: String): String = run {
        val keySpec = SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        val encryptedData = cipher.doFinal(data.toByteArray())
        return@run Base64.getEncoder().encodeToString(encryptedData)
    }

    // Decrypt the data
    private fun decrypt(encryptedData: String, base64Key: String): String = run {
        val keySpec = SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        val decodedData = Base64.getDecoder().decode(encryptedData)
        val decryptedData = cipher.doFinal(decodedData)
        return@run String(decryptedData)
    }

    // Partial mask (leave the first few characters visible, encrypt the rest)
    fun mask(data: String, revealedLen: Int, secretKey: String): String = run {
        if (revealedLen > data.length) {
            throw ServiceException(
                ReturnCode.INVALID,
                ": Encrypted length cannot be longer than the data length"
            )
        }

        if (revealedLen == data.length) {
            return@run data
        }

        val toEncrypt = data.substring(0, data.length - revealedLen) // Part to be encrypted
        val suffix = data.substring(data.length - revealedLen) // Unmasked part

        // Encrypt the remaining part
        val encrypted = encrypt(toEncrypt, secretKey)

        // Concatenate the unmasked prefix with the encrypted part
        return@run "$encrypted\u001F$suffix"
    }

    // Decrypt and combine with the visible part
    fun unmask(data: String, secretKey: String): String = run {
        val parts = data.split("\u001F")

        if (parts.size != 2) {
            return@run data
        }

        val encrypted = parts.first() // Encrypted part
        val suffix = parts.last() // Unmasked part

        // Decrypt the remaining part
        val decrypted = decrypt(encrypted, secretKey)

        // Concatenate the unmasked prefix with the decrypted part
        return@run "$decrypted$suffix"
    }
}