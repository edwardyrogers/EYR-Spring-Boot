package com.eyr.demo.common.exceptions

import com.eyr.demo.common.constants.AppErrorCode
import com.eyr.demo.common.models.ApiModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


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

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<Any> {
        return ResponseEntity
            .internalServerError()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = AppErrorCode.UNKNOWN_REASON,
                        msg = exception.message,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }
}