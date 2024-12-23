package com.eyr.demo.core.data.crypto

import com.eyr.demo.core.objects.RequestMetadata
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value
import java.util.*

open class CryptoEncryptedJsonSerialiser(
    private val cryptoService: CryptoService,
) : JsonSerializer<Any>() {

    @Value("\${backend-core.crypto.enabled:false}")
    private val enabled: Boolean = false

    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) = run {
        if (!enabled) {
            generator.writeString(value.toString())
            return
        }

        generator.writeString(
            Base64.getEncoder().encodeToString(
                cryptoService.doAESEncryption(
                    key = RequestMetadata.get().cptKey,
                    data = value.toString().toByteArray(),
                )
            )
        )
    }
}