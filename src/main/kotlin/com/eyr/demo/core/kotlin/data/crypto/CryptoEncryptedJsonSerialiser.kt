package cc.worldline.common.data.crypto

import cc.worldline.common.objects.RequestMetadata
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class CryptoEncryptedJsonSerialiser(
    private val cryptoService: CryptoService,
) : JsonSerializer<Any>() {

    @Value("\${cryptography.enabled}")
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