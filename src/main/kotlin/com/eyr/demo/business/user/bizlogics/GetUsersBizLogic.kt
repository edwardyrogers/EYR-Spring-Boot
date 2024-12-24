package com.eyr.demo.business.user.bizlogics

import com.eyr.demo.api.user.UserModel
import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.eyr.demo.core.interfaces.BizLogicFetchMany
import com.eyr.demo.core.models.BizLogicModel
import com.eyr.demo.datasource.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class GetUsersBizLogic(
    private val _repository: UserRepository
) : BizLogicFetchMany {
    override fun <T> execute(
        req: BizLogicModel.REQ.Data<T>
    ): BizLogicModel.RES.Paginated<T> = run {
        if (req !is REQ) throw ServiceException(
            ReturnCode.INVALID, "${req::class.simpleName} Request Model"
        )

        val response = when (val payload = req.payload) {

            is UserModel.GetUsersREQ.ByStatus -> _repository.findAllByStatus(
                PageRequest.of(
                    0, 20
                ),
                payload.status,
                req.projection
            )

            is UserModel.GetUsersREQ.All -> _repository.findAllBy(
                PageRequest.of(
                    0, 20
                ),
                req.projection
            )
        }

        return@run RES(
            data = response
        )
    }

    data class REQ<PROJ>(
        val payload: UserModel.GetUsersREQ,

        override val projection: Class<PROJ>
    ) : BizLogicModel.REQ.Data<PROJ>(projection)

    data class RES<T>(
        override val data: Page<T>
    ) : BizLogicModel.RES.Paginated<T>(data)
}