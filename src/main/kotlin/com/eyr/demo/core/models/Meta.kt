package cc.worldline.common.models

import cc.worldline.common.constants.ClientChannel
import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.data.crypto.CryptoService
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.utils.KeyUtils
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
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
    open val lang: String = "The metadata is not given from the request",

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
            "lang" to lang,
            "deviceId" to deviceId,
            "sessionId" to sessionId,
            "client" to client,
        )
    }

    fun copy(
        lang: String = this.lang,
        sessionId: String = this.sessionId,
        deviceId: String = this.deviceId,
        cptKey: ByteArray = this.cptKey,
        apiKey: String = this.apiKey,
        client: ClientChannel = this.client
    ) = Meta(
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
                LOGGER.info("No cpt key")
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
}
