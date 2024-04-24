package com.eyr.demo.common.exceptions

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.constants.AppErrorCode
import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<Any> {
        val mapper = ObjectMapper()
        val logMap = exception.fieldErrors.associate { it.field to it.defaultMessage }
        val prettied = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(logMap)

        LOGGER.error("--- [${AppConstant.ON_GOING.uppercase()}] MethodArgumentNotValidException: $prettied")

        return ResponseEntity
            .badRequest()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = AppErrorCode.BODY_VALIDATION_FAILED,
                        msg = exception.body.detail,
                        stacktrace = exception.stackTrace.contentToString(),
                    ),
                ),
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMismatchedInputException(exception: HttpMessageNotReadableException): ResponseEntity<Any> {
        LOGGER.error("--- [${AppConstant.ON_GOING.uppercase()}] MismatchedInputException: ${exception.message}")

        return ResponseEntity
            .badRequest()
            .body(
                ApiModel.Response<ApiModel.Payload>(
                    error = ApiModel.Error(
                        code = AppErrorCode.CANNOT_DESERIALIZE_VALUE,
                        msg = exception.message,
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