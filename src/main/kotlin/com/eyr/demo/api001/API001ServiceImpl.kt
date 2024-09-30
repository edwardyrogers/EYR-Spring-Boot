package com.eyr.demo.api001

import cc.worldline.common.HsbcKeyUtil
import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.data.fiegnclients.eyrspringbootmicro.EYRSpringBootMicroClient
import com.eyr.demo.common.data.fiegnclients.eyrspringbootmicro.EYRSpringBootMicroModel
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel
import org.springframework.stereotype.Service

@Service
class API001ServiceImpl(
    private val eyrSpringBootMicroClient: EYRSpringBootMicroClient
) : API001Service {
    override fun api001001(body: API001Model.API001001REQ): ApiModel.Response<API001Model.API001001RES> {
//        throw RequestFailedException(
//            code = ReturnCode.VALIDATION_FAILED,
//        )

        return run {
            ApiModel.Response(
                payload = API001Model.API001001RES(
                    greeting = run {
                        val res = eyrSpringBootMicroClient.api000001(
                            apiKey = HsbcKeyUtil.getApiKey(),
                            body = EYRSpringBootMicroModel.API000001REQ(
                                req = body.name
                            )
                        )

                        res.payload.res
                    }
                )
            )
        }
    }

    override fun api001002(body: API001Model.API001002REQ): ApiModel.Response<API001Model.API001002RES> {
        return run {
            ApiModel.Response(
                payload = API001Model.API001002RES(
                    greeting = "Hello ${body.name}"
                )
            )
        }
    }
}