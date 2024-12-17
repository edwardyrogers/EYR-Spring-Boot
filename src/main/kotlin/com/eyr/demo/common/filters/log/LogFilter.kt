package com.eyr.demo.common.filters.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Filter
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Filter(name = "LogFilter")
@Order(0)
class LogFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) = run {
        if (!request.requestURI.startsWith("/api/v2/")) {
            // If the request URI does not start with "/api/v2/",
            // skip processing of this filter and continue with the next one in the chain
            chain.doFilter(request, response)
            return@run
        }

        // Wrap the request and response objects to add custom behavior
        val reqWrapped = LogReqWrapper(request) // Create a wrapped version of the request
        val resWrapped = LogResWrapper(response) // Create a wrapped version of the response

        reqWrapped.log() // Log the request
        chain.doFilter(reqWrapped, resWrapped) // Proceed with the chain
        resWrapped.log(request) // Log the response
    }
}