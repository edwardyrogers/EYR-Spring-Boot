package com.eyr.demo.common.handlers

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel
import com.eyr.demo.common.models.ApiModel.Payload
import com.eyr.demo.common.models.ApiModel.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.function.Consumer
import javax.crypto.BadPaddingException


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        exception: Exception,
    ): ResponseEntity<Response<ApiModel.Error>> {
        exception.printStackTrace()

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                Response(
                    payload = ApiModel.Error(
                        code = ReturnCode.ACCESS_DENIED,
                        msg = exception.localizedMessage,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleUsernameNotFoundException(exception: UsernameNotFoundException): ResponseEntity<Any> {
        exception.printStackTrace()

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                Response<Payload>(
                    payload = ApiModel.Error(
                        code = ReturnCode.ACCESS_DENIED,
                        msg = exception.localizedMessage,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(RequestFailedException::class)
    fun handleRequestFailedException(exception: RequestFailedException): ResponseEntity<Response<Payload>> {
        exception.printStackTrace()

        return ResponseEntity
            .badRequest()
            .body(
                Response(
                    payload = ApiModel.Error(
                        code = exception.code,
                        msg = exception.msg,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    fun handleValidationExceptions(exception: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors: MutableList<String> = mutableListOf()

        exception.bindingResult.allErrors.forEach(
            Consumer { error: ObjectError ->
                val fieldName = (error as FieldError).field
                val errorMessage = error.getDefaultMessage()
                errors += "[$fieldName]: $errorMessage"
            }
        )

        exception.printStackTrace()

        return ResponseEntity
            .badRequest()
            .body(
                Response<Payload>(
                    payload = ApiModel.Error(
                        code = ReturnCode.VALIDATION_FAILED,
                        msg = errors.joinToString(", "),
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException): ResponseEntity<Response<Payload>> {
        exception.printStackTrace()

        val maybeBadPaddingException = exception.cause?.cause

        if (maybeBadPaddingException is BadPaddingException) {
            return ResponseEntity.status(420).body(
                Response(
                    payload = ApiModel.Error(
                        code = ReturnCode.GENERAL_ERROR,
                        msg = exception.message,
                        stacktrace = exception.stackTrace.contentToString(),
                    )
                )
            )
        }

        return ResponseEntity
            .badRequest()
            .body(
                Response(
                    payload = ApiModel.Error(
                        code = ReturnCode.GENERAL_ERROR,
                        msg = exception.message,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Response<Payload>> {
        exception.printStackTrace()

        return ResponseEntity
            .internalServerError()
            .body(
                Response(
                    payload = ApiModel.Error(
                        code = ReturnCode.GENERAL_ERROR,
                        msg = exception.localizedMessage,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }
}