package cc.worldline.common.data.mask

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value

class MaskJsonSerialiser : JsonSerializer<Any>() {

    @Value("\${cryptography.enabled}")
    private val enabled: Boolean = false

    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) = run {
        if (!enabled) {
            generator.writeString(value.toString())
            return@run
        }

        if (value.toString().length < 3) {
            generator.writeString(value.toString())
            return@run
        }

        generator.writeString(
            MaskUtils.mask(
                value.toString(),
                3,
                MaskUtils.generateKey()
            )
        )
    }
}