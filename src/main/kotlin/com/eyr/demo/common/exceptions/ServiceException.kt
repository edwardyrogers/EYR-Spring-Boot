package com.eyr.demo.common.exceptions

import com.eyr.demo.common.interfaces.Code
import com.eyr.demo.common.interfaces.Error
import com.eyr.demo.common.objects.RequestMetadata

/**
 * Exception class for handling service-related errors that are not logged into ELK (Elasticsearch, Logstash, Kibana).
 *
 * @property code An instance of [Code] representing the specific error code.
 * @property msgParams An array of params for the message parameters if needed.
 * @property where An optional parameter indicating the context or location where the error occurred.
 *                 Defaults to a new instance of [Object] if no parameter is provided.
 * @property message A descriptive message providing additional details about the error.
 *
 * This class extends [RuntimeException], allowing it to be thrown without being declared
 * in method signatures, making it suitable for various service-related error-handling scenarios.
 */
class ServiceException(
    override val code: Code,
    override vararg val msgParams: Any,
    override val where: Any = Object(),
    override val message: String = code.messageIn(RequestMetadata.get().obtainLocale(), *msgParams)
) : Error<Code>, RuntimeException()