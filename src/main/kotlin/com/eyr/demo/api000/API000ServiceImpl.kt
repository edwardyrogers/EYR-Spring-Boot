package com.eyr.demo.api000

import com.eyr.demo.common.data.repos.dance.DanceModel
import com.eyr.demo.common.data.repos.dance.DanceRepository
import com.eyr.demo.common.models.ApiModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
                DanceModel(name = request.danceName, type = request.danceType)
            )

            ApiModel.Response(
                payload = API000Model.API000002RES(
                    result = true
                )
            )
        }.getOrElse {
            ApiModel.Response(
                payload = API000Model.API000002RES(
                    result = false
                )
            )
        }
    }
}