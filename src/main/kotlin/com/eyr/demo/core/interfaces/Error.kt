package com.eyr.demo.core.interfaces

/**
 * Interface representing an error with a specific code.
 *
 * ==========================================
 *
 * Example of implementing the `Error` interface with [RuntimeException]:
 * ```kotlin
 *  class ServiceException(
 *      override val code: Code,
 *      override val where: Any = Object(),
 *      override val message: String = code.messageIn(RequestMetadata.get().locale)
 *  ) : Error<Code>, RuntimeException()
 * ```
 *
 * @param C A type parameter that must extend the [Code] class.
 *           The implementing classes must provide a `code` property of this type.
 *
 * @property code The error code associated with this error instance.
 * @property msgParams params using for message parameters.
 * @property where An object that indicates the context or location where the error occurred.
 */
interface Error<C : Code> {
    val code: C
    val msgParams: Any
    val where: Any
}