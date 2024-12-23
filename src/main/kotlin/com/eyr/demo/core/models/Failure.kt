package cc.worldline.common.models

import cc.worldline.common.interfaces.Payload
import java.time.Instant

/**
 * A open class representing a failure model.
 *
 * @property timestamp The time when the failure occurred, defaulting to the current time in ISO-8601 format.
 * @property code The specific failure code value associated with the failure.
 * @property message An optional message providing more details about the failure.
 * @property stacktrace An optional string representation of the stack trace, useful for debugging.
 */
open class Failure(
    open val timestamp: String = Instant.now().toString(),
    open val code: String = "",
    open val message: String = "",
    open val stacktrace: String? = null
) : Payload