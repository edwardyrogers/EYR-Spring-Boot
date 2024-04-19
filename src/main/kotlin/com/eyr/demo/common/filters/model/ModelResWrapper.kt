package com.eyr.demo.common.filters.model

import com.eyr.demo.common.constants.AppReturnCode
import com.eyr.demo.common.servlets.streams.HttpBodyServletOutputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import java.io.*


class ModelResWrapper(
    private val response: HttpServletResponse
) : HttpServletResponseWrapper(response) {

    private val byteArrayOutputStream = ByteArrayOutputStream()

    fun writeOutputStream() {
        val mapper = ObjectMapper()
        val res = mapper.readValue(
            byteArrayOutputStream.toString(),
            object : TypeReference<HashMap<String, Any>>() {}
        )
        val modified = mapper.writeValueAsString(
            mapOf(
                "code" to AppReturnCode.valueOf(res["code"] as String).code,
                "payload" to res["payload"]
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