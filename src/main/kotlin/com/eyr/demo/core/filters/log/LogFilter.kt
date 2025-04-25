package com.eyr.demo.core.filters.log

import com.eyr.demo.common.constants.CommonConst
import com.eyr.demo.core.models.Failure
import com.eyr.demo.core.models.Meta
import com.eyr.demo.core.objects.RequestMetadata
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Filter
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Filter(name = "LogFilter")
@Order(0)
class LogFilter(
    private val environment: Environment,
) : OncePerRequestFilter() {

    @Value("\${server.servlet.encoding.charset:UTF-8}")
    private val charset: String = "UTF-8"

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) = runCatching {
        request.characterEncoding = charset
        response.characterEncoding = charset

        // Wrap the request and response objects to add custom behavior
        val reqWrapped = LogReqWrapper(request, environment) // Create a wrapped version of the request
        val resWrapped = LogResWrapper(response, environment) // Create a wrapped version of the response

        reqWrapped.log() // Log the request
        chain.doFilter(reqWrapped, resWrapped) // Proceed with the chain
        resWrapped.log(request) // Log the response
    }.getOrElse {
        val failure = Failure(
            code = "${CommonConst.SERVICE_NAME}00000",
            message = it.message ?: "Unknown.",
            stacktrace = it.stackTraceToString()
        )

        val failureStr = MAPPER.writeValueAsString(
            mapOf(
                "meta" to runCatching { RequestMetadata.get().toMutableMap() }.getOrElse { Meta().toMutableMap() },
                "payload" to failure.toMutableMap()
            )
        )

        // Set the status code and content type
        response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR // or 500, or whatever you need
        response.contentType = "application/json"
        response.characterEncoding = charset

        // Write the custom response
        response.outputStream.write(failureStr.toByteArray(charset(charset)))
    }

    companion object {
        private val MAPPER = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }
}