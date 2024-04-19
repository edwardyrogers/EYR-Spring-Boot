package com.eyr.demo.api000

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
class API000Controller {

    @Autowired
    lateinit var service: API000Service

    companion object {
        private val logger = LoggerFactory.getLogger(API000Controller::class.java)
    }

    @PostMapping("API000001")
    fun api000001(
        @RequestBody request: API000Model.API000001REQ
    ): ApiModel.Response<API000Model.API000001RES>  {
        return service.api000001(request)
    }
}