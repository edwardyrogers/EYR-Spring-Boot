package cc.worldline.common.filters.model

import cc.worldline.common.models.Meta
import cc.worldline.common.objects.RequestMetadata
import cc.worldline.common.streams.HttpBodyServletInputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.util.StreamUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * A wrapper class for [HttpServletRequest] that allows manipulation of the request data.
 *
 * This class extends [HttpServletRequestWrapper] to override methods for accessing the request's
 * input stream and reader. It captures the request body as a byte array for later processing.
 *
 * @param request The original [HttpServletRequest] that is being wrapped.
 */
class ModelReqWrapper(
    private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {

    private val body: ByteArray = StreamUtils.copyToByteArray(
        this.request.inputStream
    )

    /**
     * Sets the current metadata from the request body into the [RequestMetadata].
     *
     * This method parses the request body as a JSON object and retrieves the "meta" field,
     * converting it into an instance of [Meta]. The metadata is then stored using
     * [RequestMetadata.set].
     */
    fun setCurrentMeta() = run {
        // Parse the request body into a map
        val req: Map<String, Any> = MAPPER.readValue(
            body,
            object : TypeReference<Map<String, Any>>() {} // Type reference for deserialization
        )

        // Extract and convert the "meta" field into a Meta object, if it exists
        val meta: Meta? = req["meta"]?.let {
            MAPPER.convertValue(it, Meta::class.java) // Convert the meta map to a Meta object
        }

        // Set the extracted metadata into the RequestMetadata
        RequestMetadata.set(meta)
    }

    /**
     * Returns the input stream for reading the request body.
     *
     * This method overrides the default getInputStream method to return a new input stream
     * that reads from the stored byte array, allowing multiple accesses to the request body.
     *
     * @return A [ServletInputStream] that reads from the captured request body.
     */
    override fun getInputStream(): ServletInputStream = HttpBodyServletInputStream(
        inputStream = ByteArrayInputStream(body)
    )

    /**
     * Returns a reader for reading the request body.
     *
     * This method overrides the default getReader method to return a new reader
     * that reads from the stored byte array, allowing multiple accesses to the request body.
     *
     * @return A [BufferedReader] that reads from the captured request body.
     */
    override fun getReader(): BufferedReader = BufferedReader(
        InputStreamReader(
            ByteArrayInputStream(body)
        )
    )

    companion object {
        private val MAPPER = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }
}