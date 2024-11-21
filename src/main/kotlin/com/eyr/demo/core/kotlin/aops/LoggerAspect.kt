package cc.worldline.common.aops

import cc.worldline.common.constants.CoreConst
import cc.worldline.common.models.Request
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(0)
class LoggerAspect {

    // Before the controller method is executed, log the request body
    @Before(CoreConst.MIDDLEWARE_CONDITION)
    fun logRequest(joinPoint: JoinPoint) = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request ?: return@run
        val requestBody = joinPoint.args.firstOrNull() as? Request<*> ?: return@run

        val method = request.method
        val url = request.requestURI

        if (!url.startsWith("/api/v2/")) return@run

        val prettyReq = MAPPER.writeValueAsString(requestBody)

        LOGGER.info("--> [${method}] $url $prettyReq")
    }

    // After the controller method is executed, log the response body
    @AfterReturning(
        value = CoreConst.MIDDLEWARE_CONDITION,
        returning = "response"
    )
    fun logResponse(response: Any) = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request ?: return@run

        val method = request.method
        val url = request.requestURI

        if (!url.startsWith("/api/v2/")) return@run

        val prettyRes = MAPPER.writeValueAsString(response)
        val responseMap = response as? Map<*, *>? ?: return@run
        val isSuccess = responseMap["success"] as? Boolean ?: false

        if (isSuccess) {
            LOGGER.info("<-- [${method}] $url $prettyRes")
        } else {
            LOGGER.error("x-- [${method}] $url $prettyRes")
        }
    }

    // Handle exceptions if you want to log errors in the response body
    @AfterThrowing(
        value = CoreConst.MIDDLEWARE_CONDITION,
        throwing = "exception"
    )
    fun logException(exception: Throwable) = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request ?: return@run

        val method = request.method
        val url = request.requestURI

        if (!url.startsWith("/api/v2/")) return@run

        val exData = mapOf(
            "success" to false,
            "message" to exception.localizedMessage,
            "stacktrace" to exception.stackTrace.contentToString()
        )

        val prettyRes = MAPPER.writeValueAsString(exData)

        LOGGER.error("x-- [${method}] $url $prettyRes")

        exception.printStackTrace()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LoggerAspect::class.java)

        private val MAPPER = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
        }
    }
}