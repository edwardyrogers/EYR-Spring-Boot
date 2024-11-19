package cc.worldline.common.streams

import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

class HttpBodyServletInputStream(
    private val inputStream: InputStream
) : ServletInputStream() {

    override fun read(): Int = inputStream.read()

    override fun isFinished(): Boolean = inputStream.available() == 0

    override fun isReady(): Boolean = true

    override fun setReadListener(p0: ReadListener?) {}
}