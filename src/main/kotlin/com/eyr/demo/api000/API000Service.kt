package com.eyr.demo.api000

import com.eyr.demo.common.models.ApiModel

interface API000Service {
    fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES>
    fun api000002(request: API000Model.API000002REQ): ApiModel.Response<API000Model.API000002RES>
    fun api000003(request: API000Model.API000003REQ): ApiModel.Response<API000Model.API000003RES>
    fun api000004(request: API000Model.API000004REQ): ApiModel.Response<API000Model.API000004RES>
}