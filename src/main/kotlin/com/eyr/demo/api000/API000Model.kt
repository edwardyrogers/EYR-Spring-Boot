package com.eyr.demo.api000

import com.eyr.demo.common.models.ApiModel
import com.fasterxml.jackson.annotation.JsonProperty

class API000Model {
    data class API000001REQ  (
        val name: String = ""
    )

    data class API000001RES (
        @JsonProperty("greeting")
        val greeting: String = ""
    ) : ApiModel.Payload()
}