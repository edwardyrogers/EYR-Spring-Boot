package com.eyr.demo.api000

import com.eyr.demo.common.constants.AppErrorCode
import com.eyr.demo.common.data.repos.dance.DanceModel
import com.eyr.demo.common.data.repos.dance.DanceRepository
import com.eyr.demo.common.data.repos.dance.DanceUtil
import com.eyr.demo.common.exceptions.RequestFailedException
import com.eyr.demo.common.models.ApiModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.lang.IllegalArgumentException

@Service
class API000ServiceImpl : API000Service {

    @Autowired
    private lateinit var danceRepository: DanceRepository

    override fun api000001(request: API000Model.API000001REQ): ApiModel.Response<API000Model.API000001RES> {
        return run {
            ApiModel.Response(
                payload = API000Model.API000001RES(
                    dances = danceRepository.findAll()
                )
            )
        }
    }

    override fun api000002(request: API000Model.API000002REQ): ApiModel.Response<API000Model.API000002RES> {
        return runCatching {
            danceRepository.save(
                DanceModel(
                    name = request.danceName,
                    type = DanceUtil.DanceType.valueOf(request.danceType)
                )
            )

            ApiModel.Response(
                payload = API000Model.API000002RES(
                    result = true
                )
            )
        }.getOrElse {
            when(it) {
                is IllegalArgumentException -> throw RequestFailedException(
                    code = AppErrorCode.BODY_VALIDATION_FAILED,
                    msg = "Dance type ${request.danceType} is out of constant"
                )
                else -> ApiModel.Response(
                    payload = API000Model.API000002RES(
                        result = false
                    )
                )
            }
        }
    }
}