@file:Suppress("unused")

package cc.worldline.common.hendlers

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ELKErrorRecordException
import cc.worldline.common.interfaces.ErrorService
import cc.worldline.common.models.Failure
import cc.worldline.common.models.Response
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
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
     * Handles the `HttpMessageNotReadableException` thrown during deserialization issues in HTTP requests.
     *
     * This method captures the exception when the request payload cannot be properly read or mapped to the expected
     * model. It specifically handles the case of unrecognized fields or missing values for required parameters,
     * providing a structured response to the client.
     *
     * @param ex The `HttpMessageNotReadableException` that was thrown during deserialization.
     * @param handler The `HandlerMethod` that represents the controller method being invoked.
     *
     * @return A `Response<Failure>` object containing details about the error, including:
     *         - `code`: A formatted error code based on the handler method and error type.
     *         - `message`: A descriptive error message indicating the cause of the failure, such as a missing or unrecognized field.
     *         - `stacktrace`: The stack trace of the exception for debugging purposes.
     *
     * @note This function can handle both unrecognized fields (via `UnrecognizedPropertyException`)
     *       and missing values for required fields, formatting them into user-friendly error messages.
     */
    open fun httpMessageNotReadableException(
        ex: HttpMessageNotReadableException,
        handler: HandlerMethod
    ): Response<Failure> = run {
        // "Missing or null value for required parameter: 'nationalId' [payload.nationalId]"
        Response.failure(
            Failure(
                code = _errorService.formatErrorCode(
                    ReturnCode.INVALID.value,
                    handler.beanType.kotlin
                ),
                message = run {
                    val cause = ex.cause

                    val parameter = if (cause is MismatchedInputException) {
                        cause.path.joinToString(".") { it.fieldName ?: "unknown" }
                    } else {
                        "unknown"
                    }

                    when (cause) {
                        is UnrecognizedPropertyException -> "$parameter: is not recognized"
                        else -> "$parameter: must not be missing or null value"
                    }
                },
                stacktrace = ex.stackTrace.contentToString()
            ),
        )
    }

    /**
     * Handles the `MethodArgumentNotValidException` thrown when a method argument fails validation.
     *
     * This method is invoked when the validation of a method argument annotated with
     * `@Valid` or `@Validated` fails, typically due to constraints such as `@NotNull`,
     * `@Size`, etc. It processes the validation errors and returns a structured response
     * that includes the field names and their associated error messages.
     *
     * @param ex The `MethodArgumentNotValidException` that was thrown, containing details
     *           of the validation errors.
     * @param handler The `HandlerMethod` representing the controller method being invoked.
     *
     * @return A `Response<Failure>` object containing:
     *         - `code`: A formatted error code based on the handler method and error type.
     *         - `message`: A combined message listing all the invalid fields and their error messages.
     *         - `stacktrace`: The stack trace of the exception for debugging purposes.
     *
     * @note This function iterates over the validation errors in the `bindingResult` and formats
     *       each field error message. The error message for each field is joined by a " | " separator.
     */
    open fun methodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        handler: HandlerMethod
    ): Response<Failure> = run {
        Response.failure(
            Failure(
                code = _errorService.formatErrorCode(
                    ReturnCode.INVALID.value,
                    handler.beanType.kotlin
                ),
                message = ex.bindingResult
                    .fieldErrors
                    .joinToString(" | ") {
                        "${it.field}: ${it.defaultMessage ?: "invalid value"}"
                    },
                stacktrace = ex.stackTrace.contentToString()
            ),
        )
    }

    /**
     * Handles the `ELKErrorRecordException`, typically thrown when there is an error related to
     * processing or recording an event in ELK (Elasticsearch, Logstash, Kibana).
     *
     * This method returns a response with an internal server error status (`500`) and includes
     * the failure details contained within the `ELKErrorRecordException`. This can be useful for
     * handling situations where the application encounters issues while interacting with the ELK stack.
     *
     * @param ex The `ELKErrorRecordException` that was thrown, which contains the failure details
     *           related to the ELK processing error.
     *
     * @return A `ResponseEntity<Response<Failure>>` containing:
     *         - The HTTP status `500 INTERNAL_SERVER_ERROR`.
     *         - The `Failure` object encapsulating error details from the exception.
     *
     * @note The function wraps the `Failure` object from the exception into the response body,
     *       and the status code is set to `500` to indicate a server-side error.
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
     * Handles generic exceptions that occur within the application, typically those that do not
     * fall into any specific category of error.
     *
     * This method is invoked for unexpected errors that may arise during the execution of business logic,
     * where no specific exception handler has been defined. It returns a response with an internal
     * server error status (`500`), including the error details such as the exception's message
     * and stack trace for debugging purposes.
     *
     * @param ex The `Exception` that was thrown, containing details about the unexpected error.
     * @param handler The `HandlerMethod` representing the controller method where the exception occurred.
     *
     * @return A `ResponseEntity<Response<Failure>>` containing:
     *         - The HTTP status `500 INTERNAL_SERVER_ERROR`.
     *         - A `Failure` object with:
     *           - `code`: A formatted error code based on the handler method and error type.
     *           - `message`: The localized message of the exception.
     *           - `stacktrace`: The stack trace of the exception for detailed error tracking.
     *
     * @note This function is designed to catch unexpected exceptions and provide a consistent error response
     *       with essential debugging information, including the stack trace, which can be helpful during
     *       development and troubleshooting.
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