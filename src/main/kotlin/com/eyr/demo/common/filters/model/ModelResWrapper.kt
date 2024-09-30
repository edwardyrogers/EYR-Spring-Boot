package com.eyr.demo.common.filters.model

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.models.ApiModel
import com.eyr.demo.common.servlets.streams.HttpBodyServletOutputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter


class ModelResWrapper(
    private val response: HttpServletResponse
) : HttpServletResponseWrapper(response) {

    private val byteArrayOutputStream = ByteArrayOutputStream()

    fun writeOutputStream() {
        if ("$byteArrayOutputStream".isEmpty()) {
            return
        }

        val mapper = ObjectMapper()

        val res = mapper.readValue(
            "$byteArrayOutputStream",
            object : TypeReference<HashMap<String, Any>>() {}
        )

        val modified = mapper.writeValueAsString(
            mapOf(
                "payload" to run {
                    val payload = when (val payloadData = res["payload"]) {
                        is HashMap<*, *> -> {
                            // Safely cast the keys and values
                            payloadData.filterKeys { it is String }
                                .mapKeys { it.key as String }
                                .mapValues { it.value as Any }
                        }
                        else -> {
                            // Handle the case where data is not a HashMap
                            emptyMap()
                        }
                    }

                    // Modify the payload if "code" exists
                    val updatedPayload = payload.toMutableMap()  // Create a mutable copy
                    if (updatedPayload["code"] != null) {
                        updatedPayload["code"] = ReturnCode.valueOf(updatedPayload["code"] as String).code
                    }

                    updatedPayload
                },
            )
        )

        response.outputStream.write(modified.toByteArray())
    }

    override fun getOutputStream(): ServletOutputStream {
        return HttpBodyServletOutputStream(
            outputStream = this.byteArrayOutputStream,
        )
    }

    override fun getWriter(): PrintWriter {
        return PrintWriter(
            OutputStreamWriter(
                this.byteArrayOutputStream,
                this.response.characterEncoding
            )
        )
    }

    override fun flushBuffer() = writer.flush()

    override fun toString(): String = writer.toString()
}