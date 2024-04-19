package com.eyr.demo.api001

import com.eyr.demo.common.constants.AppConstant
import com.eyr.demo.common.models.ApiModel
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AppConstant.PATH_API_V1)
class API001Controller {

    @Autowired
    lateinit var service: API001Service

    companion object {
        private val logger = LoggerFactory.getLogger(API001Controller::class.java)
    }

    @PostMapping("API001001")
    fun api001001(
        @RequestBody request: API001Model.API001001REQ
    ): ApiModel.Response<API001Model.API001001RES> {
        return service.api001001(request)
    }
}