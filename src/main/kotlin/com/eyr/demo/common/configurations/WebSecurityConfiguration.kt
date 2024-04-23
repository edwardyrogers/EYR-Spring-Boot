package com.eyr.demo.common.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class WebSecurityConfiguration {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return run {
            http.csrf { csrf -> csrf.disable() }
                .authorizeHttpRequests { authorize -> authorize.anyRequest().authenticated() }
                .httpBasic(withDefaults())
                .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

            http.build()
        }
    }
}