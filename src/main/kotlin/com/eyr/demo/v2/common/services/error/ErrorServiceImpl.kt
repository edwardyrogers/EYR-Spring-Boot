package cc.worldline.customermanagement.v2.common.services.error

import cc.worldline.common.interfaces.ErrorService
import cc.worldline.customermanagement.v2.common.constants.CSMReturnCode
import cc.worldline.customermanagement.v2.common.constants.CommonConst
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class ErrorServiceImpl : ErrorService {
    override fun formatErrorCode(errCode: String, where: KClass<*>): String = run {
        "${CommonConst.SERVICE_NAME}${CSMReturnCode.findControllerCode(where)}${errCode}"
    }
}