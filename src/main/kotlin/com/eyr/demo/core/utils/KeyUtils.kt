package com.eyr.demo.core.utils

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for generating keys.
 */
object KeyUtils {

    private var dateFormatSeed: String? = null

    fun initialize(
        dateFormatSeed: String?,
    ) {
        this.dateFormatSeed = dateFormatSeed
    }

    /**
     * Generates a unique API key based on the current date and a UUID.
     *
     * @return AN SHA-256 hashed string representing the API key.
     */
    fun generateKey(): String = run {
        // Format to only include day, month, and year
        val dateFormat = getRandomDateFormatByDay()
        val dateFormatted = SimpleDateFormat(dateFormat, Locale.getDefault())
        val dateString = dateFormatted.format(Date())

        // Generate a UUID based on the current date
        val uuid = UUID.nameUUIDFromBytes(dateString.toByteArray())

        // Create a hash of the combined string
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest("PREFIX-$dateString-SUFFIX-$uuid".toByteArray())

        // Convert the byte array to a hex string
        return@run digest.joinToString("") { String.format("%02x", it) }
    }

    private fun getRandomDateFormatByDay(): String = run {
        // List of possible date format patterns
        val dateFormats = listOf(
            "yyyy-MM-dd",
            "MM-dd-yyyy",
            "dd-MM-yyyy",

            "yyyy/MM/dd",
            "MM/dd/yyyy",
            "dd/MM/yyyy",

            "yyyyMMdd",
            "MMddyyyy",
            "ddMMyyyy"
        )

        // Use the current date as a seed
        val today = SimpleDateFormat(
            dateFormatSeed ?: "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())

        val seed = today.hashCode() // Generate a hash based on today's date

        // Use the seed to consistently pick a format for today
        val random = Random(seed.toLong())

        return@run dateFormats[random.nextInt(dateFormats.size)]
    }
}