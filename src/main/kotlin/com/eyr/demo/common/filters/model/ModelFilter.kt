package com.eyr.demo.common.filters.model

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component


@Order(1)
@Component("modelFilter")
class ModelFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val reqWrapped = ModelReqWrapper(request as HttpServletRequest)
        val resWrapped = ModelResWrapper(response as HttpServletResponse)

        chain!!.doFilter(reqWrapped, resWrapped)

        resWrapped.writeOutputStream()
    }
}

