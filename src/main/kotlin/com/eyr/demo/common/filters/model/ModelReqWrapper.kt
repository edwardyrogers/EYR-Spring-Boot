package com.eyr.demo.common.filters.model

import com.eyr.demo.common.servlets.streams.HttpBodyServletInputStream
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.util.StreamUtils
import java.io.*


class ModelReqWrapper(
    private val request: HttpServletRequest
) : HttpServletRequestWrapper(request) {

    private val body: ByteArray = StreamUtils.copyToByteArray(
        this.request.inputStream
    )

    override fun getInputStream(): ServletInputStream {
        return HttpBodyServletInputStream(
            inputStream =  ByteArrayInputStream(body)
        )
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(
            InputStreamReader(
                ByteArrayInputStream(body)
            )
        )
    }
}