package com.eyr.demo.common.streams

import jakarta.servlet.ServletOutputStream
import jakarta.servlet.WriteListener
import java.io.ByteArrayOutputStream

class HttpBodyServletOutputStream(
    private val _outputStream: ByteArrayOutputStream,
) : ServletOutputStream() {

    val outputStream get() = _outputStream

    override fun write(b: Int) = _outputStream.write(b)

    override fun isReady(): Boolean = true

    override fun setWriteListener(p0: WriteListener?) {}

    override fun flush() = _outputStream.flush()
}