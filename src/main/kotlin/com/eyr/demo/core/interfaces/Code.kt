package com.eyr.demo.core.interfaces

import java.text.MessageFormat
import java.util.*

/**
 * Interface representing a code with an associated message.
 *
 * Implementing classes must provide:
 * - [value]: A string representation of the code.
 * - [msgKey]: A string that describes the key for retrieving the message.
 *
 * ==========================================
 *
 * Example of implementing the `Code` interface:
 * ```kotlin
 * enum class ReturnCode(
 *     override val value: String,
 *     override val msgKey: String
 * ) : Code {
 *     ACCESS_DENIED("401", "unauthorized")
 * }
 * ```
 *
 * The `messageIn` function retrieves the message based on the provided locale.
 * If no locale is specified, it defaults to English.
 */
interface Code {
    val value: String
    val msgKey: String

    val resourceBundleName: String
        get() = "core_messages"

    fun messageIn(locale: Locale = Locale.ENGLISH, vararg params: Any): String = run {
        val resourceBundle = ResourceBundle.getBundle(resourceBundleName, locale)
        val message = resourceBundle.getString(msgKey)
        return@run MessageFormat.format(message, *params)
    }
}