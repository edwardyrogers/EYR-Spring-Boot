package com.eyr.demo.api001

import com.eyr.demo.common.models.ApiModel

interface API001Service {
    fun api001001(request: API001Model.API001001REQ): ApiModel.Response<API001Model.API001001RES>

    fun api001002(request: API001Model.API001002REQ): ApiModel.Response<API001Model.API001002RES>
}