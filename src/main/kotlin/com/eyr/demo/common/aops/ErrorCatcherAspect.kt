package com.eyr.demo.common.aops

import com.eyr.demo.common.constants.CoreConst
import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.ELKErrorRecordException
import com.eyr.demo.common.exceptions.ServiceException
import com.eyr.demo.common.interfaces.Code
import com.eyr.demo.common.interfaces.ErrorService
import com.eyr.demo.common.models.Failure
import com.eyr.demo.common.models.Response
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(0)
class ErrorCatcherAspect(
    private val _errorService: ErrorService,
) {
    @Around(CoreConst.MIDDLEWARE_CONDITION)
    fun handleErrors(joinPoint: ProceedingJoinPoint): Any? = run {
        val requestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val request = requestAttributes?.request

        if (request != null && !request.requestURI.startsWith("/api/v2/")) {
            return@run joinPoint.proceed()
        }

        runCatching {
            // Proceed with the original execution
            return@run joinPoint.proceed()
        }.getOrElse {
            it.printStackTrace()

            return@run when (it) {
                is ServiceException -> Response.failure(
                    Failure(
                        code = _errorService.formatErrorCode(it.code.value, joinPoint.target::class),
                        message = it.message,
                        stacktrace = it.stackTrace.contentToString()
                    ),
                )

                else -> throw ELKErrorRecordException(
                    code =
                    if (it is ELKErrorRecordException)
                        it.code
                    else
                        ReturnCode.UNEXPECTED_ERROR,

                    where = joinPoint.target::class,
                    message = it.message ?: "",
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
}