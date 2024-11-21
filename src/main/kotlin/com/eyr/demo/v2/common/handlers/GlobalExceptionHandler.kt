package cc.worldline.customermanagement.v2.common.handlers

import cc.worldline.common.exceptions.ELKErrorRecordException
import cc.worldline.common.hendlers.GlobalExceptionCoreHandler
import cc.worldline.common.interfaces.ErrorService
import cc.worldline.common.models.Failure
import cc.worldline.common.models.Response
import cc.worldline.customermanagement.common.bean.ApiResponse
import cc.worldline.customermanagement.common.bean.ServiceError
import cc.worldline.customermanagement.common.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.HandlerMethod
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
class GlobalExceptionHandler(
    errorService: ErrorService,
) {
    private val _globalExceptionCoreHandler = GlobalExceptionCoreHandler(errorService)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
        handler: HandlerMethod
    ): ResponseEntity<*> = run {
        if (request.requestURI.startsWith("/api/v2/")) {
            _globalExceptionCoreHandler.methodArgumentNotValidException(ex, handler)
        } else {
            val details = ex.bindingResult.allErrors.map { it.defaultMessage }
            ResponseEntity.badRequest()
                .body(
                    ApiResponse.failure(
                        ServiceError(
                            "400",
                            details.joinToString(System.lineSeparator()),
                            null
                        )
                    )
                )
        }
    }

    @ExceptionHandler(ELKErrorRecordException::class)
    fun elkErrorRecordException(
        ex: ELKErrorRecordException
    ): ResponseEntity<Response<Failure>> = run {
        _globalExceptionCoreHandler.elkErrorRecordException(ex)
    }

    @ExceptionHandler(Exception::class)
    fun exception(
        ex: Exception,
        request: HttpServletRequest,
        handler: HandlerMethod
    ): ResponseEntity<*> = run {
        if (request.requestURI.startsWith("/api/v2/")) {
            _globalExceptionCoreHandler.exception(ex, handler)
        } else {
            ex.printStackTrace()
            if (ex is CustomException) {
                ApiResponse.failure(
                    ServiceError(
                        ex.code ?: "500",
                        ex.localizedMessage,
                        null
                    )
                )
                    .let { ResponseEntity.status(ex.status ?: HttpStatus.INTERNAL_SERVER_ERROR.value()).body(it) }

            } else {
                val status = HttpStatus.INTERNAL_SERVER_ERROR
                ApiResponse.failure(
                    ServiceError(
                        status.value().toString(),
                        ex.message ?: ex.localizedMessage,
                        null
                    )
                )
                    .let { ResponseEntity.status(status).body(it) }
            }
        }
    }
}
