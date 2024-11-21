package cc.worldline.customermanagement.v2.common.filters.apikey

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.models.Request
import cc.worldline.common.utils.KeyUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(3)
class ApiSecurityAspect {
    @Before(CONDITION)
    fun logRequest(joinPoint: JoinPoint) = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request ?: return@run

        val url = request.requestURI

        if (!url.startsWith("/api/v2/")) return@run

        val requestBody = joinPoint.args.firstOrNull() as? Request<*> ?: return@run

//        if (requestBody.meta.apiKey != KeyUtils.getApiKey()) throw ServiceException(
//            ReturnCode.ACCESS_DENIED
//        )
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