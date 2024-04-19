package com.eyr.demo.common.models

import com.eyr.demo.common.constants.AppReturnCode
import com.fasterxml.jackson.annotation.JsonProperty

class ApiModel {
    open class Payload

    data class Response<T: Payload> (
        @JsonProperty("code")
        val code: AppReturnCode,
        @JsonProperty("payload")
        val payload: T? = null,
        @JsonProperty("error")
        val error: String? = null,
        @JsonProperty("extra")
        val extra: String? = null,
    )
}