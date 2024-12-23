package com.eyr.demo.core.configs

import com.eyr.demo.core.utils.KeyUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
open class KeyConfig {
    @Value("\${backend-core.key-util.date-format-seed:dd-MM-yyyy}")
    private val dateFormatSeed: String = "dd-MM-yyyy"

    @EventListener
    fun onApplicationReady(event: ApplicationReadyEvent) {
        LOGGER.info("Initialise key")
        KeyUtils.initialize(dateFormatSeed)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KeyConfig::class.java)
    }
}