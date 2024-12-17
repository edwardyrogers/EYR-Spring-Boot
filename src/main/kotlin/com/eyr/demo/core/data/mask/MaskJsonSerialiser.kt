package com.eyr.demo.common.data.mask

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value

class MaskJsonSerialiser : JsonSerializer<Any>() {

    @Value("\${cryptography.enabled}")
    private val enabled: Boolean = false

    @Value("\${cryptography.masking-length}")
    private val maskingLen: Int = 1

    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) = run {
        if (!enabled) {
            generator.writeString(value.toString())
            return@run
        }

        if (value.toString().length < maskingLen) {
            generator.writeString(value.toString())
            return@run
        }

        generator.writeString(
            MaskUtils.mask(
                value.toString(),
                maskingLen,
                MaskUtils.generateKey()
            )
        )
    }
}