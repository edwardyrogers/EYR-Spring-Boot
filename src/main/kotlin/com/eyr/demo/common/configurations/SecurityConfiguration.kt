package com.eyr.demo.common.configurations

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.data.repositories.user.UserHelper.UserRole
import com.eyr.demo.common.filters.error.ErrorFilter
import com.eyr.demo.common.filters.jwt.JwtFilter
import com.eyr.demo.common.filters.jwt.JwtService
import com.eyr.demo.common.filters.log.LogFilter
import com.eyr.demo.common.filters.model.ModelFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val logFilter: LogFilter,
    private val modelFilter: ModelFilter,
    private val jwtFilter: JwtFilter,
    private val errorFilter: ErrorFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val jwtService: JwtService,
) {
    companion object {
        val WHITE_LIST_URL = arrayOf(
            "/${AppConstant.PATH_API_V1}/API000001",
            "/${AppConstant.PATH_API_V1}/API000002",
            "/${AppConstant.PATH_API_V1}/API000003",
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return run {
            http.csrf { csrf -> csrf.disable() }
                .authorizeHttpRequests { req ->
                    req
                        .requestMatchers(*WHITE_LIST_URL)
                        .permitAll()
                        .requestMatchers("${AppConstant.PATH_API_V1}/**")
                        .hasAnyRole(UserRole.REGULAR.name, UserRole.ADMIN.name)
                        .anyRequest()
                        .authenticated()
                }
                .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
//                .logout { logout ->
//                    logout
//                        .logoutUrl("/${AppConstant.PATH_API_V1}/API000003")
//                        .addLogoutHandler(jwtService)
//                        .logoutSuccessHandler { request, response, authentication ->
//                            SecurityContextHolder.clearContext()
//                        }
//                }
                .addFilterBefore(modelFilter, JwtFilter::class.java)
                .addFilterBefore(logFilter, ModelFilter::class.java)
                .build()
        }
    }
}