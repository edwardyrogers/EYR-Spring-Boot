package com.eyr.demo.common.filters.log

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component("logFilter")
class LogFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val reqWrapped = LogReqWrapper(request)
        val resWrapped = LogResWrapper(response)

        reqWrapped.log()
        filterChain.doFilter(reqWrapped, resWrapped)
        resWrapped.log(request)
    }
}