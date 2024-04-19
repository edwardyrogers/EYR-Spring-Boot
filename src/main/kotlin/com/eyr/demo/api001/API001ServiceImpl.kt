package com.eyr.demo.api001

import com.eyr.demo.common.constants.AppReturnCode
import com.eyr.demo.common.exceptions.BadRequestException
import com.eyr.demo.common.models.ApiModel

import org.springframework.stereotype.Service

@Service
class API001ServiceImpl : API001Service {
    override fun api001001(request: API001Model.API001001REQ): ApiModel.Response<API001Model.API001001RES> {
        throw BadRequestException(
            code = AppReturnCode.FAILED,
            message = "Bed request",
        )
    }
}