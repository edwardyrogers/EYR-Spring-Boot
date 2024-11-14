@file:Suppress("unused")

package com.eyr.demo.core.kotlin.hendlers

import com.eyr.demo.core.kotlin.constants.ReturnCode
import com.eyr.demo.core.kotlin.exceptions.ELKErrorRecordException
import com.eyr.demo.core.kotlin.interfaces.ErrorService
import com.eyr.demo.core.kotlin.models.Failure
import com.eyr.demo.core.kotlin.models.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.HandlerMethod

/**
 * GlobalExceptionCoreHandler is responsible for handling various types of exceptions
 * within the application and formatting them into standardized failure responses.
 *
 * @property _errorService The service used for formatting error codes and managing error-related operations.
 */
open class GlobalExceptionCoreHandler(
    private val _errorService: ErrorService,
) {
    /**
     * Handles MethodArgumentNotValidException, typically thrown during validation failures
     * of method arguments in request bodies.
     *
     * @param ex The exception containing validation errors.
     * @param handler The handler method information, used for formatting the error code.
     * @return ResponseEntity containing a Response object with failure details including
     *         formatted error code, validation messages, and stack trace.
     */
    open fun methodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        handler: HandlerMethod
    ): ResponseEntity<Response<Failure>> = run {
        val details = ex.bindingResult.allErrors.map { it.defaultMessage }

        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                Response.failure(
                    Failure(
                        code = _errorService.formatErrorCode(
                            ReturnCode.INVALID.value,
                            handler.beanType.kotlin
                        ),
                        message = details.joinToString(System.lineSeparator()),
                        stacktrace = ex.stackTrace.contentToString()
                    ),
                )
            )
    }

    /**
     * Handles ELKErrorRecordException, typically thrown for specific ELK-related errors.
     *
     * @param ex The exception containing a pre-formatted failure response.
     * @return ResponseEntity containing the failure details from the exception with
     *         status 500 (INTERNAL_SERVER_ERROR).
     */
    open fun elkErrorRecordException(
        ex: ELKErrorRecordException
    ): ResponseEntity<Response<Failure>> = run {
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ex.failure
            )
    }

    /**
     * Handles generic exceptions, providing a standardized response for unexpected errors.
     *
     * @param ex The exception to handle.
     * @param handler The handler method information, used for formatting the error code.
     * @return ResponseEntity containing a Response object with failure details including
     *         formatted error code, exception message, and stack trace with status 500
     *         (INTERNAL_SERVER_ERROR).
     */
    open fun exception(
        ex: Exception,
        handler: HandlerMethod
    ): ResponseEntity<Response<Failure>> = run {
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                Response.failure(
                    Failure(
                        code = _errorService.formatErrorCode(
                            ReturnCode.UNEXPECTED_ERROR.value,
                            handler.beanType.kotlin
                        ),
                        message = ex.localizedMessage,
                        stacktrace = ex.stackTrace.contentToString()
                    ),
                )
            )

    }
}