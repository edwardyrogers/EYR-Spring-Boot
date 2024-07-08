package com.eyr.demo.common.jsonisers

import com.eyr.demo.common.services.CryptoService
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.*

@Component
class CryptoDecryptedJsonDeserialiser(
    private val cryptoService: CryptoService,
) : JsonDeserializer<String>() {
    @Value("\${cryptography.enabled}")
    private val enabled: Boolean = false

    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = run {
        if (!enabled || parser.valueAsString.isEmpty()) return parser.valueAsString

        val encryptedBytes = Base64.getDecoder().decode(parser.valueAsString)
        val encryptedKey = encryptedBytes.sliceArray(0 until 256)
        val encryptedData = encryptedBytes.sliceArray(256 until encryptedBytes.size)
        val decryptedKey = cryptoService.doRSADecryption(encryptedKey)

        val attributes = RequestContextHolder.getRequestAttributes()

        if (attributes != null && attributes is ServletRequestAttributes) {
            val key = attributes.getAttribute(
                "CurrentAESKey",
                RequestAttributes.SCOPE_REQUEST,
            ) as ByteArray? ?: byteArrayOf()

            // Frontend would send its aes key per request if there is any request field
            // that is needed to be encrypted there and decrypted here
            // And no matter how many fields are needed to be done with crypto actions
            // the Aes key sent is same per request; and therefore we only set it once per request
            // This key is going to be used when it comes to so encryption in the response
            // if fields are needed to be done with crypto actions
            if (key.isEmpty()) {
                attributes.setAttribute(
                    "CurrentAESKey",
                    decryptedKey,
                    RequestAttributes.SCOPE_REQUEST,
                )
            }
        }

        String(
            cryptoService.doAESDecryption(
                key = decryptedKey,
                data = encryptedData,
            )
        )
    }
}