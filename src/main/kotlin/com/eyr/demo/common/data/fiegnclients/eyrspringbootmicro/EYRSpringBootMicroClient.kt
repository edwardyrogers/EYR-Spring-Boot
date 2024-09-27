package com.eyr.demo.common.data.fiegnclients.eyrspringbootmicro

import com.eyr.demo.common.models.ApiModel
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "eyr-spring-boot-micro-client", url = "http://localhost:8082")
interface EYRSpringBootMicroClient {
    @PostMapping("/api/v1/API000001")
    fun api000001(
        @RequestHeader("X-API-KEY") apiKey: String,
        @RequestBody body: EYRSpringBootMicroModel.API000001REQ
    ): ApiModel.Response<EYRSpringBootMicroModel.API000001RES>
}