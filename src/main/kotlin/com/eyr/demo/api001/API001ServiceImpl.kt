package com.eyr.demo.api001

import com.eyr.demo.api000.API000Model
import com.eyr.demo.common.constants.AppReturnCode
import com.eyr.demo.common.models.ApiModel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class API001ServiceImpl : API001Service {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(API001ServiceImpl::class.java)
    }

    override fun api001001(request: API001Model.API001001REQ): ApiModel.Response<API001Model.API001001RES> {
        return run {
            ApiModel.Response(
                code = AppReturnCode.SUCCESS,
                payload = API001Model.API001001RES(
                    greeting = "Hello World ${request.name}"
                )
            )
        }
    }
}