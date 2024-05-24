package com.eyr.demo.common.data.repositories.token

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize

class TokenHelper {
    @JsonSerialize(using = TokenTypeSerializer::class)
    enum class TokenType {
        BEARER;

        val index get() = ordinal.toString()
    }

    private class TokenTypeSerializer : JsonSerializer<TokenType>() {
        override fun serialize(p0: TokenType?, p1: JsonGenerator?, p2: SerializerProvider?) {
            p1?.writeNumber(p0?.index)
        }
    }
}