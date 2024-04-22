package com.eyr.demo.api000

import com.eyr.demo.common.models.ApiModel
import org.springframework.stereotype.Service

@Service
class API000ServiceImpl : API000Service {
    override fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
        return run {
            ApiModel.Response(
                payload = API000Model.API000001RES(
                    greeting = "Hello World ${request.name}"
                )
            )
        }
    }
}