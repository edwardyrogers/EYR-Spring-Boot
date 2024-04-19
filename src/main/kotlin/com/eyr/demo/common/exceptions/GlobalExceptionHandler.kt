package com.eyr.demo.common.exceptions


import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.IOException

@ControllerAdvice
class GlobalExceptionHandler {
    companion object {
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

//    @ExceptionHandler(HelloWorldException::class)
//    fun handleStudentNotFoundException(exception: HelloWorldException): ResponseEntity<Any> {
//        logger.info("Edward Global Exception 1")
//        return ResponseEntity
//            .status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body(exception.message)
//    }

    @ExceptionHandler(Exception::class)
    fun exception(exception: Exception): ResponseEntity<Any> {
        logger.info("Edward Global Exception 2")
        return ResponseEntity
            .status(500)
            .body(exception.message)
    }
}