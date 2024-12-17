package com.eyr.demo.common.filters.model

import com.eyr.demo.common.objects.RequestMetadata
import com.eyr.demo.common.streams.HttpBodyServletOutputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter

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

    private val byteArrayOutputStream = ByteArrayOutputStream()

    /**
     * Writes the modified output stream based on the original response.
     *
     * This method reads the input stream from the wrapped request and the content of the byte array
     * output stream, modifies the response body to include the success status, metadata, and payload,
     * and then writes the modified response to the output stream.
     *
     */
    fun writeOutputStream() = run {
        if ("$byteArrayOutputStream".isEmpty()) return@run

        val res = MAPPER.readValue(
            "$byteArrayOutputStream",
            object : TypeReference<HashMap<String, Any>>() {}
        )

        val modified = MAPPER.writeValueAsString(
            mapOf(
                "success" to res["success"],
                "meta" to RequestMetadata.get().toMutableMap(),
                "payload" to res["payload"],
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
    override fun getOutputStream(): ServletOutputStream = HttpBodyServletOutputStream(
        this.byteArrayOutputStream,
    )

    /**
     * Returns a writer for writing the response body.
     *
     * This method overrides the default getWriter method to return a new writer that writes to
     * the byte array output stream, allowing the response body to be captured and modified.
     *
     * @return A [PrintWriter] that writes to the captured byte array output stream.
     */
    override fun getWriter(): PrintWriter = PrintWriter(
        OutputStreamWriter(
            this.byteArrayOutputStream,
            this.response.characterEncoding
        )
    )

    override fun flushBuffer() = writer.flush()

    override fun toString(): String = writer.toString()

    companion object {
        private val MAPPER = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }
}