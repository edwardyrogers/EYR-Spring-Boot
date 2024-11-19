package cc.worldline.common.aops

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ELKErrorRecordException
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.Code
import cc.worldline.common.interfaces.ErrorService
import cc.worldline.common.models.Failure
import cc.worldline.common.models.Response
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(2)
class ErrorCatcherAspect(
    private val _errorService: ErrorService,
) {
    @Around(CONDITION)
    fun handleErrors(joinPoint: ProceedingJoinPoint): Any? = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request

        if (request != null && !request.requestURI.startsWith("/api/v2/")) {
            return@run joinPoint.proceed()
        }

        runCatching {
            // Proceed with the original execution
            joinPoint.proceed()
        }.getOrElse {
            when (it) {
                is ServiceException -> Response.failure(
                    Failure(
                        code = _errorService.formatErrorCode(it.code.value, joinPoint.target::class),
                        message = it.localizedMessage,
                        stacktrace = it.stackTrace.contentToString()
                    ),
                )

                else -> throw ELKErrorRecordException(
                    code = ReturnCode.UNEXPECTED_ERROR,
                    where = joinPoint.target::class,
                    message = it.localizedMessage,
                    customFormattedCodeCallback = { code: Code ->
                        _errorService.formatErrorCode(
                            code.value,
                            joinPoint.target::class
                        )
                    }
                )
            }
        }
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