package com.eyr.demo.core.data.cache

import com.eyr.demo.core.models.Request
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hazelcast.nio.ObjectDataInput
import com.hazelcast.nio.ObjectDataOutput
import com.hazelcast.nio.serialization.StreamSerializer

class HazelcastSerialiser : StreamSerializer<Any> {

    private val objectMapper = jacksonObjectMapper()

    override fun write(out: ObjectDataOutput, obj: Any) = run {
        // Convert object to JSON string
        val jsonString = objectMapper.writeValueAsString(obj)
        // Write JSON string to the output
        out.writeString(jsonString)
    }

    override fun read(input: ObjectDataInput): Any = run {
        // Read byte array from the input
        val byteArray = input.readString()

        // Deserialize JSON string back into an object
        val type = objectMapper.typeFactory.constructParametricType(
            Request::class.java,
            Any::class.java
        )

        objectMapper.readValue(byteArray, type)
    }

    override fun getTypeId(): Int = 1000

    override fun destroy() {}
}