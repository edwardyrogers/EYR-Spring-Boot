package com.eyr.demo.core.configs

import com.eyr.demo.core.objects.CoreParam
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Configuration
open class CoreConfig {

    @PostConstruct
    fun init() {
        getReadyCoreVol()
    }

    private fun getReadyCoreVol() = run {
        val properties = Properties()
        val inputStream = object {}.javaClass.getResourceAsStream("/common.properties")
        inputStream.use { properties.load(it) }
        val coreVol = properties.getProperty("build.timestamp", "")
        val instant = Instant.parse(coreVol.ifEmpty { "1970-01-01T00:00:00Z" })
        val localDateTime = instant.atZone(ZoneId.of("Asia/Taipei"))
        val vol = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(localDateTime)
        CoreParam.coreVol = vol
        LOGGER.info("CoreVol: $vol")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CoreConfig::class.java)
    }
}