package com.eyr.demo.common.aops

import com.eyr.demo.common.constants.CoreConst
import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.ServiceException
import com.eyr.demo.common.filters.log.LogFilter
import com.eyr.demo.common.models.Request
import com.eyr.demo.common.utils.KeyUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(1)
class ApiAuthCheckerAspect(
    private val _environment: Environment,
) {
    @Around(CoreConst.MIDDLEWARE_CONDITION)
    fun handleApiAuth(joinPoint: ProceedingJoinPoint): Any? = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request

        if (request != null && !request.requestURI.startsWith("/api/v2/")) {
            return@run joinPoint.proceed()
        }

        val requestBody = joinPoint.args.firstOrNull() as? Request<*> ?: throw ServiceException(
            ReturnCode.INVALID, "Request body..."
        )

        if (_environment.activeProfiles.contains("dev")) {
            return@run joinPoint.proceed()
        }

        LOGGER.info("--- API auth checking result: ${requestBody.meta.apiKey != KeyUtils.generateKey()}")

        if (requestBody.meta.apiKey != KeyUtils.generateKey()) {
            throw ServiceException(
                ReturnCode.ACCESS_DENIED,
                message = "Invalid API key"
            )
        }

        return@run joinPoint.proceed()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LogFilter::class.java)
    }
}
