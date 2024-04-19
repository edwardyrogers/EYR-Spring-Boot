package com.eyr.demo.api000

import com.eyr.demo.common.constants.AppReturnCode
import com.eyr.demo.common.models.ApiModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class API000ServiceImpl : API000Service {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(API000ServiceImpl::class.java)
    }

    override fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
        return run {
            ApiModel.Response(
                code = AppReturnCode.SUCCESS,
                payload = API000Model.API000001RES(
                    greeting = "Hello World ${request.name}"
                )
            )
        }
    }
}