package cc.worldline.common.data.mask

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.beans.factory.annotation.Value

open class MaskJsonSerialiser : JsonSerializer<Any>() {

    @Value("\${backend-core.mask.enabled:false}")
    private val enabled: Boolean = false

    @Value("\${backend-core.mask.revealed-length:5}")
    private val maskingLen: Int = 5

    override fun serialize(value: Any, generator: JsonGenerator, provider: SerializerProvider) = run {
        if (!enabled) {
            generator.writeString(value.toString())
            return@run
        }

        if (value.toString().length < maskingLen) {
            generator.writeString(value.toString())
            return@run
        }

        generator.writeString(
            MaskUtils.mask(
                value.toString(),
                maskingLen,
                MaskUtils.generateKey()
            )
        )
    }
}