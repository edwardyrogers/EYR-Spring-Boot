package com.eyr.demo.common.filters.error

import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.handlers.GlobalExceptionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
        response.status
        val entity = when (it) {
            is AccessDeniedException -> globalExceptionHandler.handleAccessDeniedException(response, it)
            is RequestFailedException -> globalExceptionHandler.handleRequestFailedException(it)
            else -> globalExceptionHandler.handleException(Exception(it?.localizedMessage))
        }

        val mapper = ObjectMapper()
        val modified = mapper.writeValueAsString(
            mapOf(
                "payload" to entity.body?.payload,
                "error" to if (entity.body?.error != null) {
                    val err = entity.body?.error

                    mapOf(
                        "timestamp" to err?.timestamp,
                        "code" to err?.code,
                        "msg" to err?.msg,
                        "stacktrace" to err?.stacktrace
                    )
                } else null,
            )
        )

        response.outputStream.write(modified.toByteArray())
    }
}