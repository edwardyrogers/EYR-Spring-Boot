package cc.worldline.customermanagement.v2.common.mask

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class MaskJsonSerialiser : JsonSerializer<Any>() {

    @Value("\${cryptography.enabled}")
    private val _enabled: Boolean = false

    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) = run {
        if (!_enabled) {
            generator.writeString(value.toString())
            return
        }

        if (value.toString().length < 3) {
            generator.writeString(value.toString())
            return
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