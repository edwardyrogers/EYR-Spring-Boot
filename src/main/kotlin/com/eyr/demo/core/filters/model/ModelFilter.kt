package com.eyr.demo.core.filters.model

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Filter
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Filter(name = "ModelFilter")
@Order(1)
class ModelFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) = run {
        // Wrap the request and response objects to add custom behavior
        val reqWrapped = ModelReqWrapper(request) // Create a wrapped version of the request
        val resWrapped = ModelResWrapper(response) // Create a wrapped version of the response

        // Set metadata for the current request in the wrapped request object
        reqWrapped.setCurrentMeta()

        // Continue the filter chain with the wrapped request and response
        chain.doFilter(reqWrapped, resWrapped)

        // Write the output stream of the response using the wrapped request object
        resWrapped.writeOutputStream()
    }
}