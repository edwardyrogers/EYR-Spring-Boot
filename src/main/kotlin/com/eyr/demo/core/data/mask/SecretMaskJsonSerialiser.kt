package com.eyr.demo.core.data.mask

import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value

open class SecretMaskJsonSerialiser : JsonSerializer<Any>() {

    @Value("\${backend-core.mask.enabled:false}")
    private val enabled: Boolean = false

    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) = runCatching {
        if (!enabled) {
            generator.writeString(value.toString())
            return@runCatching
        }

        if (value.toString().isEmpty()) {
            generator.writeString(value.toString())
            return@runCatching
        }

        generator.writeString(
            MaskUtils.mask(
                value.toString(),
                0,
                MaskUtils.generateKey()
            )
        )
    }.getOrElse {
        throw ServiceException(
            ReturnCode.INVALID, it.message ?: it.localizedMessage
        )
    }
}