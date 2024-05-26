package com.eyr.demo.api001

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.models.ApiModel
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConstant.PATH_API_V1)
class API001Controller(
    private val service: API001Service
) {
    @PostMapping("API001001")
    @PreAuthorize("hasAuthority('admin:read')")
    fun api001001(
        @Valid @RequestBody request: API001Model.API001001REQ
    ): ApiModel.Response<API001Model.API001001RES> {
        return service.api001001(request)
    }

    @PostMapping("API001002")
    fun api001002(
        @Valid @RequestBody request: API001Model.API001002REQ
    ): ApiModel.Response<API001Model.API001002RES> {
        return service.api001002(request)
    }
}