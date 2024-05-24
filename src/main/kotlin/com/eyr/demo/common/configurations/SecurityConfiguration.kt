package com.eyr.demo.common.configurations

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.data.repositories.user.UserHelper.UserRole
import com.eyr.demo.common.filters.jwt.JwtFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val jwtFilter: JwtFilter,
    private val authenticationProvider: AuthenticationProvider,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return run {
            http.csrf { csrf -> csrf.disable() }
                .authorizeHttpRequests { authorize ->
                    authorize
                        .requestMatchers("${AppConstant.PATH_API_V1}/**")
                        .hasAnyRole(UserRole.REGULAR.name, UserRole.ADMIN.name)
                        .anyRequest()
                        .authenticated()

                }
                .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
                .httpBasic(withDefaults())

            http.build()
        }
    }
}