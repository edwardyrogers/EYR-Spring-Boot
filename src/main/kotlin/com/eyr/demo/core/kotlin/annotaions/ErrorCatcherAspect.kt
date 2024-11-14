package com.eyr.demo.core.kotlin.annotaions

import com.eyr.demo.core.kotlin.constants.ReturnCode
import com.eyr.demo.core.kotlin.exceptions.ELKErrorRecordException
import com.eyr.demo.core.kotlin.exceptions.ServiceException
import com.eyr.demo.core.kotlin.interfaces.Code
import com.eyr.demo.core.kotlin.interfaces.ErrorService
import com.eyr.demo.core.kotlin.models.Failure
import com.eyr.demo.core.kotlin.models.Response
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import java.util.*

/**
 * Aspect for catching and handling errors that occur during method execution
 * annotated with the `@ErrorCatcher` annotation.
 *
 * This aspect uses Spring AOP to intercept method calls and apply error handling logic.
 * It defines around advice that wraps the method execution in a try-catch block
 * to manage exceptions in a centralized manner.
 *
 * @property _errorService The service used for formatting error codes and handling
 *                         error-related operations.
 */
@Aspect
@Component
class ErrorCatcherAspect(
    private val _errorService: ErrorService,
) {
    /**
     * Around advice that intercepts method calls annotated with `@ErrorCatcher`.
     *
     * If an exception occurs during the method execution, it will be caught and handled.
     * The behavior depends on the type of exception:
     * - If the exception is of type `ServiceException`, it formats the error response
     *   using the `_errorService` and returns a failure response.
     * - For any other type of exception, it throws an `ELKErrorRecordException` with
     *   details about the error, including a generic error code.
     *
     * @param joinPoint The join point that allows access to the method being intercepted.
     * @param errorCatcher The annotation instance containing metadata, such as the
     *                     location where the error occurred.
     * @return The result of the method execution or an error response.
     * @throws Throwable If the method execution fails and the exception is not handled.
     */
    @Around("@within(errorCatcher) || @annotation(errorCatcher)")
    @Throws(Throwable::class)
    fun aroundAdvice(joinPoint: ProceedingJoinPoint, errorCatcher: ErrorCatcher?): Any? {
        val targetClass = joinPoint.target.javaClass
        val effectiveErrorCatcher = errorCatcher ?: targetClass.getAnnotation(ErrorCatcher::class.java)

        return runCatching {
            joinPoint.proceed()
        }.getOrElse {
            when (it) {
                is ServiceException -> Response.failure(
                    Failure(
                        code = _errorService.formatErrorCode(it.code.value, effectiveErrorCatcher.where),
                        message = it.localizedMessage,
                        stacktrace = it.stackTrace.contentToString()
                    ),
                )

                else -> throw ELKErrorRecordException(
                    code = ReturnCode.UNEXPECTED_ERROR,
                    where = effectiveErrorCatcher.where,
                    message = it.localizedMessage,
                    customFormattedCodeCallback = { code: Code ->
                        _errorService.formatErrorCode(
                            code.value,
                            effectiveErrorCatcher.where
                        )
                    }
                )
            }
        }
    }
}