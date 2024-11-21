package cc.worldline.customermanagement.v2.business.user.bizlogics

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicFetchMany
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class GetUsersBizLogic(
    private val _repository: UserRepository
) : BizLogicFetchMany {
    override suspend fun <T> execute(req: BizLogicModel.REQ.Data<T>): BizLogicModel.RES.Paginated<T> = run {
        val response = when (req) {
            is REQ.ByCustomerNumber -> _repository.findAllByCustomerNumber(
                req.pageable, req.customerNumber, req.projection
            )

            is REQ.ByIC -> _repository.findAllByIc(
                req.pageable, req.ic, req.projection
            )

            is REQ.All -> _repository.findAllBy(
                req.pageable, req.projection
            )

            else -> null
        }

        val customers = response?.takeIf { it.content.isNotEmpty() } ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        return RES(
            data = customers
        )
    }

    sealed class REQ {
        data class All<PROJ>(
            val pageable: Pageable,
            override val projection: Class<PROJ>
        ) : BizLogicModel.REQ.Data<PROJ>(projection)

        data class ByIC<PROJ>(
            val ic: String,
            val pageable: Pageable,
            override val projection: Class<PROJ>
        ) : BizLogicModel.REQ.Data<PROJ>(projection)

        data class ByCustomerNumber<PROJ>(
            val customerNumber: String,
            val pageable: Pageable,
            override val projection: Class<PROJ>
        ) : BizLogicModel.REQ.Data<PROJ>(projection)
    }

    data class RES<T>(
        override val data: Page<T>
    ) : BizLogicModel.RES.Paginated<T>(data)
}