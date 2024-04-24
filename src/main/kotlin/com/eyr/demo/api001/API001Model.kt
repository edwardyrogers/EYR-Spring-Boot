package com.eyr.demo.api001

import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

class API001Model {
    data class API001001REQ (
        @field:NotBlank
        val name: String = ""
    )

    data class API001001RES (
        @JsonProperty("greeting")
        val greeting: String = ""
    ) : ApiModel.Payload()
}