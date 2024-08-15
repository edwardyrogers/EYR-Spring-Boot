package com.eyr.demo.api001

import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

class API001Model {
    class API001001REQ

    class API001001RES : ApiModel.Payload()

    data class API001002REQ(
        @field:NotBlank
        val name: String = "",
    )

    data class API001002RES(
        @JsonProperty("greeting")
        val greeting: String
    ) : ApiModel.Payload()
}