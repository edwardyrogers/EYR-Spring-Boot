package com.eyr.demo.common.servlets.streams

import jakarta.servlet.ServletOutputStream
import jakarta.servlet.WriteListener
import java.io.ByteArrayOutputStream


class HttpBodyServletOutputStream(
    private val outputStream: ByteArrayOutputStream
) : ServletOutputStream() {

    override fun write(b: Int) = outputStream.write(b)

    override fun isReady(): Boolean = true

    override fun setWriteListener(p0: WriteListener?) {}
}