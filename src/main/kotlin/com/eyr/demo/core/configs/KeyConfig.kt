package cc.worldline.common.configs

import cc.worldline.common.utils.KeyUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class KeyConfig {
    @Value("\${backend-core.key-util.date-format-seed:dd-MM-yyyy}")
    private val dateFormatSeed: String = "dd-MM-yyyy"

    @Bean
    open fun initKey() {
        LOGGER.info("Initialise key")
        KeyUtils.initialize(dateFormatSeed)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KeyConfig::class.java)
    }
}