package com.eyr.demo.common.jsonisers

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.services.CryptoService
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.util.*

@Component
class CryptoEncryptedJsonSerialiser(
    private val cryptoService: CryptoService,
) : JsonSerializer<String>() {
    override fun serialize(value: String, generator: JsonGenerator, provider: SerializerProvider) = run {
        val key = RequestContextHolder.getRequestAttributes()?.getAttribute(
            "CurrentAESKey",
            RequestAttributes.SCOPE_REQUEST,
        ) as ByteArray?

        if (key == null) {
            throw RequestFailedException(ReturnCode.ACCESS_DENIED)
        }

        generator.writeString(
            Base64.getEncoder().encodeToString(
                cryptoService.doAESEncryption(
                    key = key,
                    data = value.toByteArray(),
                )
            )
        )
    }
}