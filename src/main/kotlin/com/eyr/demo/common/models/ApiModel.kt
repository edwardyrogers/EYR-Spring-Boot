package com.eyr.demo.common.models

import com.eyr.demo.common.constants.AppErrorCode
import com.fasterxml.jackson.annotation.JsonProperty

class ApiModel {
    open class Payload

    data class Error(
        @JsonProperty("code")
        val code: AppErrorCode,
        @JsonProperty("msg")
        val msg: String?,
        @JsonProperty("stacktrace")
        val stacktrace: String?,
    )

    data class Response<T: Payload> (
        @JsonProperty("payload")
        val payload: T? = null,
        @JsonProperty("error")
        val error: Error? = null,
    )
}