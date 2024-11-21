package cc.worldline.customermanagement.v2.common.configs

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource


@Configuration
class MessageConfig {
    @Bean
    fun hsbcCoreMessageSource(): MessageSource = run {
        val messageSource = ResourceBundleMessageSource()
        messageSource.setBasenames("classpath:hsbc_core_messages") // Loads messages from both main and library
        messageSource.setDefaultEncoding("UTF-8")
        messageSource
    }
}