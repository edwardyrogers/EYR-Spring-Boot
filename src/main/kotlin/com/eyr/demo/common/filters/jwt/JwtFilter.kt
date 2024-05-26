package com.eyr.demo.common.filters.jwt

import com.eyr.demo.common.configurations.SecurityConfiguration
import com.eyr.demo.common.data.repositories.token.TokenService
import com.eyr.demo.common.data.repositories.user.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component("jwtFilter")
class JwtFilter(
    private val jwtService: JwtService,
    private val userService: UserService,
    private val tokenService: TokenService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        run {
            if (SecurityConfiguration.WHITE_LIST_URL.contains(request.requestURI)) {
                filterChain.doFilter(request, response)

                return
            }

            val authorization =
                request.getHeader("Authorization") ?: throw AccessDeniedException("Please, provide header")

            if (!authorization.startsWith("Bearer ")) {
                filterChain.doFilter(request, response)

                throw AccessDeniedException("Please, check token type")
            }

            if (SecurityContextHolder.getContext().authentication == null) {
                val jwt = authorization.substring(7)
                val username = jwtService.getUsername(jwt)
//                val isTokenValid = tokenService.findByToken(jwt).let { token -> !token.expired && !token.revoked }
                val isTokenValid = true

                if (isTokenValid) {
                    val user = userService.findByUsername(username)
                    val authToken = UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.authorities,
                    )

                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authToken
                }
            }

            filterChain.doFilter(request, response)
        }
    }
}