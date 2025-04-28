package com.eyr.demo.core.models

import com.eyr.demo.core.constants.ClientChannel
import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.data.crypto.CryptoService
import com.eyr.demo.core.exceptions.ServiceException
import com.eyr.demo.core.objects.CoreParam
import com.eyr.demo.core.utils.KeyUtils
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import org.slf4j.LoggerFactory
import java.util.*

/**
 * An open class representing metadata associated with a response.
 *
 * @property lang The language code representing the locale. Defaults to the language of [Locale.ENGLISH].
 * @property sessionId A unique identifier for the session, useful for tracking or correlating
 *                     requests and responses.
 * @property deviceId An optional identifier for the device associated with the session.
 * @property apiKey A key to secure api services.
 * @property cptKey A key to do crypto action.
 * @property client Represent which client request for data
 */
open class Meta(
    open val coreVol: String = CoreParam.coreVol,

    open val lang: String = "",

    open val sessionId: String = "",

    open val deviceId: String = "",

    @JsonDeserialize(using = JsonCPTKeyDeserialiser::class)
    open val cptKey: ByteArray = byteArrayOf(),

    open val apiKey: String = KeyUtils.generateKey(),

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    open val client: ClientChannel = ClientChannel.UNKNOWN
) {
    fun obtainLocale(): Locale = run { Locale(lang) }

    fun toMutableMap(): MutableMap<String, *> = run {
        mutableMapOf(
            "coreVol" to coreVol,
            "lang" to lang,
            "deviceId" to deviceId,
            "sessionId" to sessionId,
            "client" to client,
        )
    }

    fun copy(
        coreVol: String = this.coreVol,
        lang: String = this.lang,
        sessionId: String = this.sessionId,
        deviceId: String = this.deviceId,
        cptKey: ByteArray = this.cptKey,
        apiKey: String = this.apiKey,
        client: ClientChannel = this.client
    ) = Meta(
        coreVol,
        lang,
        sessionId,
        deviceId,
        cptKey,
        apiKey,
        client
    )

    class JsonCPTKeyDeserialiser(
        private val _cryptoService: CryptoService,
    ) : JsonDeserializer<ByteArray>() {
        override fun deserialize(
            p: JsonParser,
            ctxt: DeserializationContext
        ): ByteArray = run {
            val cptKeyText = p.text

            if (cptKeyText.isNullOrEmpty()) {
                LOGGER.info("--- Empty cpt key")
                return@run byteArrayOf()
            }

            val encryptedBytes = try {
                Base64.getDecoder().decode(cptKeyText)
            } catch (e: IllegalArgumentException) {
                throw ServiceException(
                    ReturnCode.INVALID, ": cpt key is not a valid Base64-encoded string"
                )
            }

            if (encryptedBytes.size < 256) {
                throw ServiceException(
                    ReturnCode.INVALID, ": cpt key must be at least 256 bytes after decoding"
                )
            }

            val encryptedKey = encryptedBytes.sliceArray(0 until 256)

            return@run _cryptoService.doRSADecryption(encryptedKey)
        }

        companion object {
            private val LOGGER = LoggerFactory.getLogger(JsonCPTKeyDeserialiser::class.java)
        }
    }

    class MetaDeserializer : JsonDeserializer<Meta>() {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Meta {
            val node: JsonNode = p.codec.readTree(p)

            // Manually check each field for required values
            val lang =
                node["lang"]?.asText() ?: throw IllegalArgumentException("lang: must not be missing or null value")
            val sessionId = node["sessionId"]?.asText()
                ?: throw IllegalArgumentException("sessionId: must not be missing or null value")
            val deviceId = node["deviceId"]?.asText()
                ?: throw IllegalArgumentException("deviceId: must not be missing or null value")
            val cptKey = node["cptKey"]?.binaryValue()
                ?: throw IllegalArgumentException("cptKey: must not be missing or null value")
            val apiKey =
                node["apiKey"]?.asText() ?: throw IllegalArgumentException("apiKey: must not be missing or null value")
            val client = node["client"]?.asText()?.let { ClientChannel.valueOf(it) } ?: ClientChannel.UNKNOWN

            // Return an instance of the Meta class
            return Meta(
                lang = lang,
                sessionId = sessionId,
                deviceId = deviceId,
                cptKey = cptKey,
                apiKey = apiKey,
                client = client
            )
        }
    }

}
