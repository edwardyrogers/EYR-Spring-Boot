package com.eyr.demo.common.filters.error

import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.handlers.GlobalExceptionHandler
import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component("errorFilter")
class ErrorFilter(
    private val globalExceptionHandler: GlobalExceptionHandler
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        runCatching {
            filterChain.doFilter(request, response)
        }.getOrElse {
            handler(response, it)
        }
    }

    private fun handler(response: HttpServletResponse, it: Throwable?) {
        val entity = when (it) {
            is AccessDeniedException, is SignatureException -> {
                response.status = HttpStatus.FORBIDDEN.value()
                globalExceptionHandler.handleAccessDeniedException(Exception(it))
            }

            is RequestFailedException -> {
                response.status = HttpStatus.BAD_REQUEST.value()
                globalExceptionHandler.handleRequestFailedException(it)
            }

            else -> {
                response.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
                globalExceptionHandler.handleException(Exception(it))
            }
        }

        val mapper = ObjectMapper()
        val modified = mapper.writeValueAsString(
            mapOf(
                "payload" to entity.body?.let { body ->
                    (body.payload as? ApiModel.Error)?.let { err ->
                        mapOf(
                            "timestamp" to err.timestamp,
                            "code" to err.code,
                            "msg" to err.msg,
                            "stacktrace" to err.stacktrace
                        )
                    }
                }
            )
        )

        response.outputStream.write(modified.toByteArray())
    }
}