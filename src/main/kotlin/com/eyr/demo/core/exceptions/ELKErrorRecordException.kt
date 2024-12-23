package com.eyr.demo.core.exceptions

import com.eyr.demo.core.interfaces.Code
import com.eyr.demo.core.interfaces.Error
import com.eyr.demo.core.models.Failure
import com.eyr.demo.core.models.Response
import com.eyr.demo.core.objects.RequestMetadata

/**
 * Exception class for handling error records in ELK (Elasticsearch, Logstash, Kibana).
 *
 * @property code An instance of [Code] representing the specific error code.
 * @property msgParams An array of params for the message parameters if needed.
 * @property where An optional parameter indicating the context or location where the error occurred.
 *                 Defaults to a new instance of [Object] if no parameter is provided.
 * @property message A descriptive message providing additional information about the error.
 * @property customFormattedCodeCallback An optional string representing a custom formatted error code.
 *
 * This class extends [RuntimeException], allowing it to be thrown without being declared
 * in method signatures, making it suitable for various error-handling scenarios.
 */
class ELKErrorRecordException(
    override val code: Code,
    override vararg val msgParams: Any,
    override val where: Any = Object(),
    override val message: String = code.messageIn(RequestMetadata.get().obtainLocale(), *msgParams),
    private val customFormattedCodeCallback: (code: Code) -> String? = { null },
) : Error<Code>, RuntimeException() {
    val failure: Response<Failure>
        get() = Response.failure(
            Failure(
                code = customFormattedCodeCallback(code) ?: "NotDefinedCustomFormattedCodeCallback${code.value}",
                message = message,
                stacktrace = stackTrace.contentToString()
            ),
        )
}