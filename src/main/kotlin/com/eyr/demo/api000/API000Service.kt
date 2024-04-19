package com.eyr.demo.api000

import com.eyr.demo.common.models.ApiModel
import org.springframework.http.ResponseEntity

interface API000Service {
    fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES>
}