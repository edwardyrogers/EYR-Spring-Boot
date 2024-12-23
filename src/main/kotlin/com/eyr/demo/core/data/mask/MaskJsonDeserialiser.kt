package com.eyr.demo.core.data.mask

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value

open class MaskJsonDeserialiser : JsonDeserializer<String>() {

    @Value("\${backend-core.mask.enabled:false}")
    private val enabled: Boolean = false

    @Value("\${backend-core.mask.revealed-length:5}")
    private val maskingLen: Int = 5

    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = run {
        if (!enabled) return@run parser.valueAsString
        if (parser.valueAsString.isEmpty()) return@run parser.valueAsString
        if (parser.valueAsString.length < maskingLen) return@run parser.valueAsString

        return@run MaskUtils.unmask(
            parser.valueAsString,
            maskingLen,
            MaskUtils.generateKey()
        )
    }
}