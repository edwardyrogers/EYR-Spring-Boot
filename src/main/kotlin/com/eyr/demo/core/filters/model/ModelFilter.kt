package com.eyr.demo.core.filters.model

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.annotations.Filter
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Filter(name = "ModelFilter")
@Order(1)
class ModelFilter : OncePerRequestFilter() {

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
        val reqWrapped = ModelReqWrapper(request) // Create a wrapped version of the request
        val resWrapped = ModelResWrapper(response) // Create a wrapped version of the response

        reqWrapped.writeInputStream()

        // Continue the filter chain with the wrapped request and response
        chain.doFilter(reqWrapped, resWrapped)

        // Write the output stream of the response using the wrapped request object
        resWrapped.writeOutputStream()
    }
}