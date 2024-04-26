package com.eyr.demo.api000

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.models.ApiModel

import jakarta.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConstant.PATH_API_V1)
@PreAuthorize("hasRole('ADMIN')")
class API000Controller {

    @Autowired
    lateinit var service: API000Service

    @PostMapping("API000001")
    @PreAuthorize("hasAuthority('admin:read')")
    fun api000001(
        @Valid @RequestBody request: API000Model.API000001REQ
    ): ApiModel.Response<API000Model.API000001RES>  {
        return service.api000001(request)
    }

    @PostMapping("API000002")
    fun api000002(
        @Valid @RequestBody request: API000Model.API000002REQ
    ): ApiModel.Response<API000Model.API000002RES>  {
        return service.api000002(request)
    }
}