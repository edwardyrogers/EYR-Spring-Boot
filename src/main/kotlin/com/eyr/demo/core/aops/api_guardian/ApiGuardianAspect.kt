package com.eyr.demo.core.aops.api_guardian

import com.eyr.demo.core.constants.CoreConst
import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.eyr.demo.core.filters.log.LogFilter
import com.eyr.demo.core.objects.RequestMetadata
import com.eyr.demo.core.utils.KeyUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(1)
class ApiGuardianAspect(
    private val _environment: Environment,
) {
    @Around(CoreConst.MIDDLEWARE_CONDITION)
    fun handleApiAuth(joinPoint: ProceedingJoinPoint): Any? = run {
        if (_environment.activeProfiles.contains("dev")) {
            return@run joinPoint.proceed()
        }

        val meta = RequestMetadata.get()

        LOGGER.info("--- Is API request not authenticated: ${meta.apiKey != KeyUtils.generateKey()}")

        if (meta.apiKey != KeyUtils.generateKey()) {
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
