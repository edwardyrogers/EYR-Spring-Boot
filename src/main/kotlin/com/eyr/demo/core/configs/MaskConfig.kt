package com.eyr.demo.core.configs

import com.eyr.demo.core.data.mask.SensitiveDataHandler
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "backend-core.mask")
open class MaskConfig(
    var enabled: Boolean = false,
    var hiddenTypes: HiddenTypes = HiddenTypes()
) {
    @PostConstruct
    fun init() {
        // This is where you assign the maskConfig to the SensitiveDataHandler object
        SensitiveDataHandler.maskConfig = this

        // Optionally, you can log or perform any other initialization tasks here
        LOGGER.info("MaskConfig has been injected into SensitiveDataHandler.")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MaskConfig::class.java)
    }

    data class HiddenTypes(
        var all: HiddenTypeFields = HiddenTypeFields(),
        var head: HiddenTypeFields = HiddenTypeFields(),
        var middle: HiddenTypeFields = HiddenTypeFields(),
        var tail: HiddenTypeFields = HiddenTypeFields(),
        var twoSides: HiddenTypeFields = HiddenTypeFields() // Mapping for two-sides
    )

    data class HiddenTypeFields(
        var fields: List<String> = emptyList() // Mapping for the fields list
    )
}