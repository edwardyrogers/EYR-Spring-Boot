package com.eyr.demo.core.filters.log

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
    ) = run {
        request.characterEncoding = charset
        response.characterEncoding = charset

        // Wrap the request and response objects to add custom behavior
        val reqWrapped = LogReqWrapper(request, environment) // Create a wrapped version of the request
        val resWrapped = LogResWrapper(response, environment) // Create a wrapped version of the response

        reqWrapped.log() // Log the request
        chain.doFilter(reqWrapped, resWrapped) // Proceed with the chain
        resWrapped.log(request) // Log the response
    }
}