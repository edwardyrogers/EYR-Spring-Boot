package cc.worldline.common.utils

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for generating keys.
 */
class KeyUtils {
    companion object {

        /**
         * Generates a unique API key based on the current date and a UUID.
         *
         * @return AN SHA-256 hashed string representing the API key.
         */
        fun getApiKey(): String = run {
            // Format to only include day, month, and year
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateString = dateFormat.format(Date())

            // Generate a UUID based on the current date
            val uuid = UUID.nameUUIDFromBytes(dateString.toByteArray())

            // Create a hash of the combined string
            val md = MessageDigest.getInstance("SHA-256")
            val digest = md.digest("PREFIX-$dateString-SUFFIX-$uuid".toByteArray())

            // Convert the byte array to a hex string
            digest.joinToString("") { String.format("%02x", it) }
        }
    }
}