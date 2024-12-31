package com.eyr.demo.core.data.mask

import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value

open class SecretMaskJsonDeserialiser : JsonDeserializer<String>() {

    @Value("\${backend-core.mask.enabled:false}")
    private val enabled: Boolean = false

    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = runCatching {
        if (!enabled) return@runCatching parser.valueAsString
        if (parser.valueAsString.isEmpty()) return@runCatching parser.valueAsString

        return@runCatching MaskUtils.unmask(
            parser.valueAsString,
            MaskUtils.generateKey()
        )
    }.getOrElse {
        throw ServiceException(
            ReturnCode.INVALID, it.message ?: it.localizedMessage
        )
    }
}