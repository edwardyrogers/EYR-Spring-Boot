package com.eyr.demo.common.filters.log

import com.eyr.demo.common.servlets.streams.HttpBodyServletInputStream
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.slf4j.LoggerFactory
import org.springframework.util.StreamUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader


class LogReqWrapper(
    private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LogReqWrapper::class.java)
    }

    private val body: ByteArray = StreamUtils.copyToByteArray(
        request.inputStream
    )

    private val byteArrayInputStream = ByteArrayInputStream(
        body
    )

    fun log() {
        runCatching {
            if (body.isEmpty()) {
                return
            }

            val mapper = ObjectMapper()
            val req = mapper.readValue(
                body,
                object : TypeReference<HashMap<String, Any>>() {}
            )

            val logMap = mapOf(
                "body" to req,
            )
            val prettied = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(logMap)

            LOGGER.info("--> [${request.method}] ${request.requestURI} $prettied")
        }.getOrElse {
            LOGGER.error("--> [${request.method}] ${request.requestURI} Error occurred!!!")
            it.printStackTrace()
        }
    }

    override fun getInputStream(): ServletInputStream {
        return HttpBodyServletInputStream(
            inputStream = byteArrayInputStream
        )
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(
            InputStreamReader(
                byteArrayInputStream
            )
        )
    }
}