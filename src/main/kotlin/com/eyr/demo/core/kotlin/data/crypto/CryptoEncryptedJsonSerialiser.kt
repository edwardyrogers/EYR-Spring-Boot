package cc.worldline.common.data.crypto

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
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

        val attributes = RequestContextHolder.getRequestAttributes()

        if (attributes != null && attributes is ServletRequestAttributes) {
            // Try finding the key set at request crypto deserializer or finding on the http header
            val key = attributes.getAttribute(
                "CurrentAESKey",
                RequestAttributes.SCOPE_REQUEST,
            ) as ByteArray? ?: run {
                // If the current key is not found then try finding the key from the http header
                val currKey = attributes.request.getHeader("Curr-Key")

                if (currKey.isNotEmpty()) {
                    val encryptedBytes = Base64.getDecoder().decode(currKey)
                    val encryptedKey = encryptedBytes.sliceArray(0 until 256)
                    cryptoService.doRSADecryption(encryptedKey)
                } else {
                    throw Exception("Crypto encryption -> cannot found AES key")
                }
            }

            generator.writeString(
                Base64.getEncoder().encodeToString(
                    cryptoService.doAESEncryption(
                        key = key,
                        data = value.toString().toByteArray(),
                    )
                )
            )
        }
    }
}