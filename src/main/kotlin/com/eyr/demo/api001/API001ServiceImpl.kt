package com.eyr.demo.api001

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel

import org.springframework.stereotype.Service

@Service
class API001ServiceImpl : API001Service {
    override fun api001001(body: API001Model.API001001REQ): ApiModel.Response<API001Model.API001001RES> {
        throw RequestFailedException(
            code = ReturnCode.VALIDATION_FAILED,
        )
    }

    override fun api001002(body: API001Model.API001002REQ): ApiModel.Response<API001Model.API001002RES> {
        return run {
            ApiModel.Response(
                payload = API001Model.API001002RES(
                    greeting = "hello ${body.name}"
                )
            )
        }
    }
}