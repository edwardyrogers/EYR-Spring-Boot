package cc.worldline.common.models

import cc.worldline.common.interfaces.Payload


/**
 * A generic class representing a response structure.
 *
 * @param T the type of payload contained in the response.
 * @property success indicates whether the operation was successful.
 * @property payload the actual data payload of type T.
 */
open class Response<T : Payload>(
    open val success: Boolean = true,
    open val payload: T,
) {
    companion object {
        /**
         * Creates a successful response with no payload (like Void).
         *
         * @return a Response object indicating success with no payload.
         */
        fun success() = Response(true, Payload.Empty)

        /**
         * Creates a successful response.
         *
         * @param payload the payload data.
         * @return a Response object indicating success.
         */
        fun <T : Payload> success(payload: T) = Response(true, payload)

        /**
         * Creates a failure response.
         *
         * @param failure the error information.
         * @return a Response object indicating failure.
         */
        fun failure(failure: Failure) = Response(false, failure)
    }
}
