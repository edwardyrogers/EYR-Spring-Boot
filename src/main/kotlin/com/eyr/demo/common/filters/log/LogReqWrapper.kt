package com.eyr.demo.common.filters.log

import com.eyr.demo.common.servlets.streams.HttpBodyServletInputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.slf4j.LoggerFactory
import org.springframework.util.StreamUtils
import java.io.*


class LogReqWrapper(
    private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LogReqWrapper::class.java)
    }

    private val body: ByteArray = StreamUtils.copyToByteArray(
        this.request.inputStream
    )

    private val byteArrayInputStream = ByteArrayInputStream(
        this.body
    )

    fun log() {
        val mapper = ObjectMapper()
        val req = mapper.readValue(
            this.body,
            object: TypeReference<HashMap<String, Any>>() {}
        )
        val logMap = mapOf(
            "body" to req
        )
        val prettied = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(logMap)

        LOGGER.info("[${request.method}] --> ${request.requestURI} $prettied")
    }

    override fun getInputStream(): ServletInputStream {
        return HttpBodyServletInputStream(
            inputStream = this.byteArrayInputStream
        )
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(
            InputStreamReader(
                this.byteArrayInputStream
            )
        )
    }
}