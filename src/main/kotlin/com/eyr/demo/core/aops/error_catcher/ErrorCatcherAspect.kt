package com.eyr.demo.core.aops.error_catcher

import com.eyr.demo.core.constants.CoreConst
import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ELKErrorRecordException
import com.eyr.demo.core.exceptions.ServiceException
import com.eyr.demo.core.interfaces.Code
import com.eyr.demo.core.interfaces.ErrorService
import com.eyr.demo.core.models.Failure
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(0)
class ErrorCatcherAspect(
    private val _errorService: ErrorService,
) {
    @Around(CoreConst.MIDDLEWARE_CONDITION)
    fun handleErrors(joinPoint: ProceedingJoinPoint): Any? = run {
        runCatching {
            // Proceed with the original execution
            return@run joinPoint.proceed()
        }.getOrElse {
            it.printStackTrace()

            return@run when (it) {
                is ServiceException -> ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                        Failure(
                            code = _errorService.formatErrorCode(it.code.value, joinPoint.target::class),
                            message = it.message,
                            stacktrace = it.stackTrace.contentToString()
                        )
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
