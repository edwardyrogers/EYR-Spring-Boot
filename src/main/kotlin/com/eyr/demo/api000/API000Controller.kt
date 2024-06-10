package com.eyr.demo.api000

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.models.ApiModel
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConstant.PATH_API_V1)
class API000Controller(
    private val service: API000Service
) {
    @PostMapping("API000001")
    fun api000001(
        @Valid @RequestBody body: API000Model.API000001REQ
    ): ApiModel.Response<API000Model.API000001RES> {
        return service.api000001(body)
    }

    @PostMapping("API000002")
    fun api000002(
        @RequestBody @Valid body: API000Model.API000002REQ
    ): ApiModel.Response<API000Model.API000002RES> {
        return service.api000002(body)
    }

    @PostMapping("API000003")
    fun api000003(
        @RequestBody @Valid body: API000Model.API000003REQ
    ): ApiModel.Response<API000Model.API000003RES> {
        return service.api000003(body)
    }

    @PostMapping("API000004")
    fun api000004(
        @RequestBody @Valid body: API000Model.API000004REQ
    ): ApiModel.Response<API000Model.API000004RES> {
        return service.api000004(body)
    }
}