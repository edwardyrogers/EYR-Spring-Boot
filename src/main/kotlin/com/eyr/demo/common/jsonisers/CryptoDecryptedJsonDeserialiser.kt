package com.eyr.demo.common.jsonisers

import com.eyr.demo.common.services.CryptoService
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.util.*

@Component
class CryptoDecryptedJsonDeserialiser(
    private val cryptoService: CryptoService,
) : JsonDeserializer<String>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = run {
        val encryptedBytes = Base64.getDecoder().decode(parser.valueAsString)
        val encryptedKey = encryptedBytes.sliceArray(0 until 256)
        val encryptedData = encryptedBytes.sliceArray(256 until encryptedBytes.size)
        val decryptedKey = cryptoService.doRSADecryption(encryptedKey)

        RequestContextHolder.getRequestAttributes()?.setAttribute(
            "CurrentAESKey",
            decryptedKey,
            RequestAttributes.SCOPE_REQUEST,
        )

        String(
            cryptoService.doAESDecryption(
                key = decryptedKey,
                data = encryptedData,
            )
        )
    }
}