package com.eyr.demo.common.filters.model

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component("modelFilter")
class ModelFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val reqWrapped = ModelReqWrapper(request)
        val resWrapped = ModelResWrapper(response)

        filterChain.doFilter(reqWrapped, resWrapped)

        resWrapped.writeOutputStream()
    }
}

