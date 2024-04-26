package com.eyr.demo.common.data.repositories.dance

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize

class DanceHelper {
    @JsonSerialize(using = DanceTypeSerializer::class)
    enum class DanceType {
        NO_TYPE,
        SWING_DANCE;

        val index get() = ordinal.toString()
    }

    private class DanceTypeSerializer : JsonSerializer<DanceType>() {
        override fun serialize(p0: DanceType?, p1: JsonGenerator?, p2: SerializerProvider?) {
            p1?.writeNumber(p0?.index)
        }
    }
}
