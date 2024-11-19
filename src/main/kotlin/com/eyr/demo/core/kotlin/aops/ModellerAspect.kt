package cc.worldline.common.aops

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.models.Request
import cc.worldline.common.models.Response
import cc.worldline.common.objects.RequestMetadata
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
    @Around(CONDITION)
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

    @Around(CONDITION)
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
            "payload" to run {
                when (val payload = result.payload) {
                    is Map<*, *> -> {
                        // Safely cast keys and values
                        payload.filterKeys { it is String }
                            .mapKeys { it.key as String }
                            .mapValues { it.value }
                            .toMutableMap()
                    }

                    else -> {
                        // Handle the case where the payload is not a Map
                        mapOf("data" to payload)
                    }
                }
            }
        )
    }

    companion object {
        private const val CONDITION = "" +
                "@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
                "@annotation(org.springframework.web.bind.annotation.PatchMapping)"
    }
}