package com.eyr.demo.common.handlers

import com.eyr.demo.core.exceptions.ELKErrorRecordException
import com.eyr.demo.core.handlers.GlobalExceptionCoreHandler
import com.eyr.demo.core.interfaces.ErrorService
import com.eyr.demo.core.models.Failure
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.HandlerMethod


@RestControllerAdvice
class GlobalExceptionHandler(
    errorService: ErrorService,
) {
    private val _globalExceptionCoreHandler = GlobalExceptionCoreHandler(errorService)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest,
        handler: HandlerMethod
    ): ResponseEntity<*> = run {
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                _globalExceptionCoreHandler.httpMessageNotReadableException(
                    ex, handler,
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
        handler: HandlerMethod
    ): ResponseEntity<*> = run {
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                _globalExceptionCoreHandler.methodArgumentNotValidException(
                    ex, handler
                )
            )
    }

    @ExceptionHandler(ELKErrorRecordException::class)
    fun elkErrorRecordException(
        ex: ELKErrorRecordException
    ): ResponseEntity<Failure> = run {
        _globalExceptionCoreHandler.elkErrorRecordException(ex)
    }

    @ExceptionHandler(Exception::class)
    fun exception(
        ex: Exception,
        request: HttpServletRequest,
        handler: HandlerMethod
    ): ResponseEntity<*> = run {
        _globalExceptionCoreHandler.exception(ex, handler)
    }
}
