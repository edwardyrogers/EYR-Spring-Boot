package com.eyr.demo.common.handlers

import com.eyr.demo.common.constants.AppErrorCode
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel

import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import java.util.function.Consumer


@ControllerAdvice
class GlobalExceptionHandler {

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
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors: MutableList<String> = mutableListOf()

        ex.bindingResult.allErrors.forEach(
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
                        code = AppErrorCode.VALIDATION_FAILED,
                        msg = errors.joinToString(", "),
                        stacktrace = ex.stackTrace.contentToString(),
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
                        code = AppErrorCode.GENERAL_ERROR,
                        msg = exception.localizedMessage,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }
}