package cc.worldline.common.models

import cc.worldline.common.constants.ClientChannel
import cc.worldline.common.data.crypto.CryptoService
import cc.worldline.common.utils.KeyUtils
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
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

    @JsonDeserialize(using = JsonCPTKeyDeserializer::class)
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

    class JsonCPTKeyDeserializer(
        private val _cryptoService: CryptoService,
    ) : JsonDeserializer<ByteArray>() {
        override fun deserialize(
            p: JsonParser,
            ctxt: DeserializationContext
        ): ByteArray = run {
            val encryptedBytes = Base64.getDecoder().decode(p.text)
            val encryptedKey = encryptedBytes.sliceArray(0 until 256)
            return@run _cryptoService.doRSADecryption(encryptedKey)
        }
    }
}
