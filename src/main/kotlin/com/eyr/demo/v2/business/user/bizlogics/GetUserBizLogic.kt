package cc.worldline.customermanagement.v2.business.user.bizlogics

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicFetchOne
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class GetUserBizLogic(
    private val _repository: UserRepository
) : BizLogicFetchOne {

    override suspend fun <T> execute(req: BizLogicModel.REQ.Data<T>): BizLogicModel.RES.Single<T> = run {
        val response = when (req) {
            is REQ.ByID -> _repository.findById(
                req.id, req.projection
            ).orElse(null)

            is REQ.ByIC -> _repository.findByIc(
                req.ic, req.projection
            ).orElse(null)

            is REQ.ByUsername -> _repository.findByUsernameIgnoreCase(
                req.username, req.projection
            ).orElse(null)

            is REQ.ByCustomerNumber -> _repository.findByCustomerNumber(
                req.customerNumber, req.projection
            ).orElse(null)

            else -> null
        }

        RES(
            data = response ?: throw ServiceException(
                ReturnCode.NOT_FOUND, "find $req"
            )
        )
    }

    sealed class REQ<PROJ>(
        override val projection: Class<PROJ>
    ) : BizLogicModel.REQ.Data<PROJ>(projection) {
        data class ByID<PROJ>(
            val id: BigInteger,
            override val projection: Class<PROJ>
        ) : REQ<PROJ>(projection)

        data class ByIC<PROJ>(
            val ic: String,
            override val projection: Class<PROJ>
        ) : REQ<PROJ>(projection)

        data class ByUsername<PROJ>(
            val username: String,
            override val projection: Class<PROJ>
        ) : REQ<PROJ>(projection)

        data class ByCustomerNumber<PROJ>(
            val customerNumber: String,
            override val projection: Class<PROJ>
        ) : REQ<PROJ>(projection)
    }

    data class RES<T>(
        override val data: T
    ) : BizLogicModel.RES.Single<T>(data)
}