package com.eyr.demo.api000

import com.eyr.demo.common.data.repos.dance.DanceModel
import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.annotation.JsonProperty

import jakarta.validation.constraints.NotBlank

class API000Model {
    data class API000001REQ(
        val id: Long? = null
    )

    data class API000001RES(
        @JsonProperty("dances")
        val dances: List<DanceModel> = listOf()
    ) : ApiModel.Payload()

    data class API000002REQ(
        @field: NotBlank
        val danceName: String = "",

        @field: NotBlank
        val danceType: String = "",
    )

    data class API000002RES(
        @JsonProperty("result")
        val result: Boolean = false
    ) : ApiModel.Payload()
}