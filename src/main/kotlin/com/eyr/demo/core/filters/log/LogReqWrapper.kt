package cc.worldline.common.filters.log

import cc.worldline.common.models.Meta
import cc.worldline.common.streams.HttpBodyServletInputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.slf4j.LoggerFactory
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
class LogReqWrapper(
    private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {

    private val body: ByteArray = StreamUtils.copyToByteArray(
        this.request.inputStream
    )

    fun log() = run {
        if (body.isEmpty()) return@run

        val req: Map<String, Any> = MAPPER.readValue(
            body,
            object : TypeReference<Map<String, Any>>() {} // Type reference for deserialization
        )

        val meta: Meta = MAPPER.convertValue(
            req["meta"],
            Meta::class.java
        )

        val prettyReq = MAPPER.writeValueAsString(
            mapOf(
                "meta" to meta.toMutableMap(),
                "payload" to req["payload"]
            )
        )

        LOGGER.info("--> [${request.method}] ${request.requestURI} $prettyReq")
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
        private val LOGGER = LoggerFactory.getLogger(LogFilter::class.java)

        private val MAPPER = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }
}
