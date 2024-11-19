package cc.worldline.common.filters.model

import cc.worldline.common.objects.RequestMetadata
import cc.worldline.common.streams.HttpBodyServletOutputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

/**
 * A wrapper class for [HttpServletResponse] that allows manipulation of the response data.
 *
 * This class extends [HttpServletResponseWrapper] to override methods for accessing the response's
 * output stream and writer. It captures the response body as a byte array for processing before
 * it is sent to the client.
 *
 * @param response The original [HttpServletResponse] that is being wrapped.
 */
class ModelResWrapper(
    private val response: HttpServletResponse
) : HttpServletResponseWrapper(response) {

    private val servletOutputStream = HttpBodyServletOutputStream(
        ByteArrayOutputStream(), ::writeOutputStream
    )

    private val outputStream get() = servletOutputStream.outputStream

    /**
     * Writes the modified output stream based on the original response.
     *
     * This method reads the input stream from the wrapped request and the content of the byte array
     * output stream, modifies the response body to include the success status, metadata, and payload,
     * and then writes the modified response to the output stream.
     *
     */
    fun writeOutputStream() {
        if (this.response.isCommitted) return

        val mapper = ObjectMapper()

        val res = mapper.readValue(
            "$outputStream",
            object : TypeReference<HashMap<String, Any>>() {}
        )

        val modified = mapper.writeValueAsString(
            mapOf(
                "success" to res["success"],

                "meta" to RequestMetadata.get().toMutableMap(),

                "payload" to run {
                    val payload = when (val payload = res["payload"]) {
                        is HashMap<*, *> -> {
                            // Safely cast the keys and values
                            payload.filterKeys { it is String }
                                .mapKeys { it.key as String }
                                .mapValues { it.value }
                        }

                        else -> {
                            // Handle the case where data is not a HashMap
                            emptyMap<String, Any?>()
                        }
                    }

                    payload.toMutableMap()  // Create a mutable copy
                },
            )
        )

        response.outputStream.write(modified.toByteArray())
    }

    /**
     * Returns the output stream for writing the response body.
     *
     * This method overrides the default getOutputStream method to return a new output stream
     * that captures data into the byte array output stream.
     *
     * @return A [ServletOutputStream] that writes to the captured byte array output stream.
     */
    override fun getOutputStream(): ServletOutputStream = servletOutputStream

    /**
     * Returns a writer for writing the response body.
     *
     * This method overrides the default getWriter method to return a new writer that writes to
     * the byte array output stream, allowing the response body to be captured and modified.
     *
     * @return A [PrintWriter] that writes to the captured byte array output stream.
     */
    override fun getWriter(): PrintWriter {
        return PrintWriter(
            OutputStreamWriter(
                this.outputStream,
                this.response.characterEncoding
            )
        )
    }

    override fun flushBuffer() = writer.flush()

    override fun toString(): String = writer.toString()
}