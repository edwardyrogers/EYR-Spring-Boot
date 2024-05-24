package com.eyr.demo.api000

import com.eyr.demo.common.data.repositories.user.UserModel
import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.annotation.JsonProperty

import jakarta.validation.constraints.NotBlank

class API000Model {
    data class API000001REQ(
        @field: NotBlank
        val username: String = "",

        @field: NotBlank
        val password: String = "",

        @field: NotBlank
        val role: String = "",
    )

    data class API000001RES(
        @JsonProperty("user")
        val user: UserModel,
    ) : ApiModel.Payload()

    data class API000002REQ(
        @field: NotBlank
        val username: String = "",

        @field: NotBlank
        val password: String = "",
    )

    data class API000002RES(
        @JsonProperty("access_token")
        val accessToken: String,

        @JsonProperty("refresh_token")
        val refreshToken: String
    ) : ApiModel.Payload()
}