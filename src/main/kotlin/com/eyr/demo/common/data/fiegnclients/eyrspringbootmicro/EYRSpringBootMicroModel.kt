package com.eyr.demo.common.data.fiegnclients.eyrspringbootmicro

import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

class EYRSpringBootMicroModel {
    data class API000001REQ(
        @field: NotBlank
        val req: String = "",
    )

    data class API000001RES(
        @JsonProperty("res")
        val res: String = "",
    ) : ApiModel.Payload()
}