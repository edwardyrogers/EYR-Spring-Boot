package com.eyr.demo.common.filters.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component("jwtFilter")
class JwtFilter(
    val jwtService: JwtService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorization = request.getHeader("Authorization") ?: return
        if (!authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authorization.substring(7)
        jwtService.getUsername(jwt)

        filterChain.doFilter(request, response)
    }
}