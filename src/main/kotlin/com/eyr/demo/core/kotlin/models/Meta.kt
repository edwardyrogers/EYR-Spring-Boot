package cc.worldline.common.models

import cc.worldline.common.constants.ClientChannel
import cc.worldline.common.utils.KeyUtils
import com.fasterxml.jackson.annotation.JsonFormat
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

    open val cptKey: String = "",

    open val apiKey: String = KeyUtils.getApiKey(),

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    open val client: ClientChannel = ClientChannel.UNKNOWN
) {
    fun obtainLocale(): Locale = run { Locale(lang) }

    fun toMutableMap(): MutableMap<String, *> = run {
        mutableMapOf(
            "lang" to lang,
            "deviceId" to deviceId,
            "sessionId" to sessionId,
            "apiKey" to apiKey,
            "cptKey" to cptKey,
            "client" to client,
        )
    }
}
