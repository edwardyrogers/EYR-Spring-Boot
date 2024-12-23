package com.eyr.demo.common.error

import com.eyr.demo.common.constants.CommonConst
import com.eyr.demo.common.constants.EYRReturnCode
import com.eyr.demo.core.interfaces.ErrorService
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class ErrorServiceImpl : ErrorService {
    override fun formatErrorCode(errCode: String, where: KClass<*>): String = run {
        "${CommonConst.SERVICE_NAME}${EYRReturnCode.findControllerCode(where)}${errCode}"
    }
}