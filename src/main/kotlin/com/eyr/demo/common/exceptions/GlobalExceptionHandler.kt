package com.eyr.demo.common.exceptions

import com.eyr.demo.common.constants.AppReturnCode
import com.eyr.demo.common.models.ApiModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(exception: BadRequestException): ResponseEntity<Any> {
        return ResponseEntity
            .badRequest()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    code = exception.code,
                    error = exception.message,
                    extra = exception.stackTrace.contentToString(),
                ),
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity
            .internalServerError()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    code = AppReturnCode.FAILED,
                    error = exception.message,
                    extra = exception.stackTrace.contentToString(),
                ),
            )
    }
}