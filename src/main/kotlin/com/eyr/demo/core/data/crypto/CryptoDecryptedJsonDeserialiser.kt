package com.eyr.demo.common.data.crypto

import com.eyr.demo.common.objects.RequestMetadata
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class CryptoDecryptedJsonDeserialiser(
    private val cryptoService: CryptoService,
) : JsonDeserializer<String>() {

    @Value("\${cryptography.enabled}")
    private val enabled: Boolean = false

    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = run {
        if (!enabled) return@run parser.valueAsString
        if (parser.valueAsString.isEmpty()) return@run parser.valueAsString

        String(
            cryptoService.doAESDecryption(
                key = RequestMetadata.get().cptKey,
                data = Base64.getDecoder().decode(
                    parser.valueAsString
                ),
            )
        )
    }
}