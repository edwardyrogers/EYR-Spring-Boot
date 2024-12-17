package cc.worldline.common.data.mask

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value

class MaskJsonDeserialiser : JsonDeserializer<String>() {

    @Value("\${cryptography.enabled}")
    private val enabled: Boolean = false

    @Value("\${cryptography.masking-length}")
    private val maskingLen: Int = 1

    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = run {
        if (!enabled) return@run parser.valueAsString
        if (parser.valueAsString.isEmpty()) return@run parser.valueAsString
        if (parser.valueAsString.length < maskingLen) return@run parser.valueAsString

        return@run MaskUtils.unmask(
            parser.valueAsString,
            maskingLen,
            MaskUtils.generateKey()
        )
    }
}