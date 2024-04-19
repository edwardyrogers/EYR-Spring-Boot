package com.eyr.demo.common.filters.log

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


@Order(0)
@Component("logFilter")
class LogFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val reqWrapped = LogReqWrapper(request as HttpServletRequest)
        val resWrapped = LogResWrapper(response as HttpServletResponse)

        reqWrapped.log()
        chain!!.doFilter(reqWrapped, resWrapped)
        resWrapped.log(request)
    }
}