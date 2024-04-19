package com.eyr.demo.common.servlets.streams

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import java.io.InputStream

class HttpBodyServletInputStream(private val inputStream: InputStream) : ServletInputStream() {

    override fun read(): Int = inputStream.read();

    override fun isFinished(): Boolean = inputStream.available() == 0;

    override fun isReady(): Boolean = true

    override fun setReadListener(p0: ReadListener?) { }
}