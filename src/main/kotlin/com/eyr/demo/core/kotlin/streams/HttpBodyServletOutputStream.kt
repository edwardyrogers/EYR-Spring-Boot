package com.eyr.demo.core.kotlin.streams

import java.io.ByteArrayOutputStream
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class HttpBodyServletOutputStream(
    private val outputStream: ByteArrayOutputStream
) : ServletOutputStream() {

    override fun write(b: Int) = outputStream.write(b)

    override fun isReady(): Boolean = true

    override fun setWriteListener(p0: WriteListener?) {}
}