package com.eyr.demo.common.filters.log

import com.eyr.demo.common.servlets.streams.HttpBodyServletOutputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.json.JsonMapper
import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter


class LogResWrapper(
    private val response: HttpServletResponse
) : HttpServletResponseWrapper(response) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LogResWrapper::class.java)
    }

    private val byteArrayOutputStream = ByteArrayOutputStream()

    fun log(request: HttpServletRequest) {
        if ("$byteArrayOutputStream".isEmpty()) {
            return
        }

        val mapper = JsonMapper()

        val res = mapper.readValue(
            "$byteArrayOutputStream",
            object : TypeReference<HashMap<String, Any>>() {}
        )

        val prettied = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
            mapOf(
                "status" to "${response.status}",
                "payload" to res["payload"],
            )
        )

        if (response.status in 200..299) {
            LOGGER.info("<-- [${request.method}] ${request.requestURI} $prettied")
        } else {
            LOGGER.error("<-- [${request.method}] ${request.requestURI} $prettied")
        }

        response.outputStream.write(byteArrayOutputStream.toByteArray())
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