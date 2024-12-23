package com.eyr.demo.core.configs

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource

@Configuration
open class CoreMsgConfig {
    @Bean
    open fun coreMessageSource(): MessageSource = run {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasenames("classpath:core_messages") // Loads messages from both main and library
        messageSource.setDefaultEncoding("UTF-8")
        messageSource
    }
}