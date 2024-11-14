package com.eyr.demo.core.kotlin.models

import com.eyr.demo.core.kotlin.interfaces.Payload
import com.eyr.demo.core.kotlin.objects.RequestMetadata
import javax.validation.Valid

/**
 * An open class representing a request containing metadata and a payload.
 *
 * @param T A type parameter that must extend the [Payload] class.
 *
 * @property meta The metadata associated with the request, providing context such as session information.
 * @property payload The payload of the request, which contains the data being sent.
 */
open class Request<T : Payload>(
    @field:Valid
    open val meta: Meta,

    @field:Valid
    open val payload: T,
) {
    companion object {
        fun <T : Payload>feign(payload: T) = Request(meta = RequestMetadata.get(), payload)
    }
}