package cc.worldline.common.data.crypto

import cc.worldline.common.objects.RequestMetadata
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value
import java.util.*

open class CryptoDecryptedJsonDeserialiser(
    private val cryptoService: CryptoService,
) : JsonDeserializer<String>() {

    @Value("\${backend-core.crypto.enabled:false}")
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