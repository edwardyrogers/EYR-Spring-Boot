package com.eyr.demo.core.filters.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Filter
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
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) = run {
        // Wrap the request and response objects to add custom behavior
        val reqWrapped = LogReqWrapper(request, environment) // Create a wrapped version of the request
        val resWrapped = LogResWrapper(response, environment) // Create a wrapped version of the response

        reqWrapped.log() // Log the request
        chain.doFilter(reqWrapped, resWrapped) // Proceed with the chain
        resWrapped.log(request) // Log the response
    }
}