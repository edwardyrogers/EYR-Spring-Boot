package com.eyr.demo.common.aops

import com.eyr.demo.common.constants.CoreConst
import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.ServiceException
import com.eyr.demo.common.models.Request
import com.eyr.demo.common.models.Response
import com.eyr.demo.common.objects.RequestMetadata
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(1)
class ModellerAspect {
    @Around(CoreConst.MIDDLEWARE_CONDITION)
    fun processRequest(joinPoint: ProceedingJoinPoint): Any? = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request

        if (request != null && !request.requestURI.startsWith("/api/v2/")) {
            return@run joinPoint.proceed()
        }

        val requestBody = joinPoint.args.firstOrNull() as? Request<*> ?: throw ServiceException(
            ReturnCode.INVALID, "Request body..."
        )

        RequestMetadata.set(requestBody.meta)

        // Proceed with controller method execution
        joinPoint.proceed()
    }

    @Around(CoreConst.MIDDLEWARE_CONDITION)
    fun processResponse(joinPoint: ProceedingJoinPoint): Any = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request

        if (request != null && !request.requestURI.startsWith("/api/v2/")) {
            return@run joinPoint.proceed()
        }

        val result = joinPoint.proceed()

        if (result !is Response<*>) throw ServiceException(
            ReturnCode.INVALID,
            "Response body..."
        )

        mapOf(
            "success" to result.success,
            "meta" to RequestMetadata.get().toMutableMap(),
            "payload" to result.payload
        )
    }
}