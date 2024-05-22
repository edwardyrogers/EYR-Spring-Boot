package com.eyr.demo.common.handlers

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel
import jakarta.servlet.http.HttpServletResponse

import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import java.util.function.Consumer


@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(response: HttpServletResponse, exception: AccessDeniedException): ResponseEntity<Any> {
        response.sendError(403)

        return ResponseEntity
            .status(403)
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = ReturnCode.ACCESS_DENIED,
                        msg = exception.localizedMessage,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(RequestFailedException::class)
    fun handleRequestFailedException(exception: RequestFailedException): ResponseEntity<Any> {
        return ResponseEntity
            .badRequest()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = exception.code,
                        msg = exception.msg,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(exception: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors: MutableList<String> = mutableListOf()

        exception.bindingResult.allErrors.forEach(
            Consumer { error: ObjectError ->
                val fieldName = (error as FieldError).field
                val errorMessage = error.getDefaultMessage()
                errors += "[$fieldName]: $errorMessage"
            }
        )

        return ResponseEntity
            .badRequest()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = ReturnCode.VALIDATION_FAILED,
                        msg = errors.joinToString(", "),
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity
            .internalServerError()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = ReturnCode.GENERAL_ERROR,
                        msg = exception.localizedMessage,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }
}