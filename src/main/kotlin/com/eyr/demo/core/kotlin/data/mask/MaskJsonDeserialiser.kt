package cc.worldline.common.data.mask

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import org.springframework.beans.factory.annotation.Value

class MaskJsonDeserialiser : JsonDeserializer<String>() {

    @Value("\${cryptography.enabled}")
    private val enabled: Boolean = false

    override fun deserialize(parser: JsonParser, context: DeserializationContext): String = run {
        if (!enabled) return parser.valueAsString
        if (parser.valueAsString.isEmpty()) return parser.valueAsString

        MaskUtils.unmask(
            parser.valueAsString,
            3,
            MaskUtils.generateKey()
        )
    }
}