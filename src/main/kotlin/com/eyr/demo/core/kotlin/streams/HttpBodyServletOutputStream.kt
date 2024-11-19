package cc.worldline.common.streams

import java.io.ByteArrayOutputStream
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener

class HttpBodyServletOutputStream(
    private val _outputStream: ByteArrayOutputStream,
    private val _onFlush: () -> Unit
) : ServletOutputStream() {

    val outputStream get() = _outputStream

    override fun write(b: Int) = outputStream.write(b)

    override fun isReady(): Boolean = true

    override fun setWriteListener(p0: WriteListener?) {}

    override fun flush() = _onFlush()
}