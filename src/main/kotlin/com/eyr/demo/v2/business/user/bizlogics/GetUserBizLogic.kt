package cc.worldline.customermanagement.v2.business.user.bizlogics

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicFetchOne
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.api.customer.CustomerModel
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component

@Component
class GetUserBizLogic(
    private val _repository: UserRepository
) : BizLogicFetchOne {

    override fun <T> execute(req: BizLogicModel.REQ.Data<T>): BizLogicModel.RES.Single<T> = run {

        if (req !is REQ) throw ServiceException(
            ReturnCode.INVALID, "${req::class.simpleName} Request Model"
        )

        val response = when (val payload = req.payload) {
            is CustomerModel.GetCustomerREQ.ByID -> _repository.findById(
                payload.id, req.projection
            ).orElse(null)

            is CustomerModel.GetCustomerREQ.ByIC -> _repository.findByIc(
                payload.ic, req.projection
            ).orElse(null)

            is CustomerModel.GetCustomerREQ.ByUsername -> _repository.findByUsernameIgnoreCase(
                payload.username, req.projection
            ).orElse(null)

            is CustomerModel.GetCustomerREQ.ByCustomerNumber -> _repository.findByCustomerNumber(
                payload.customerNumber, req.projection
            ).orElse(null)
        }

        return@run RES(
            data = response ?: throw ServiceException(
                ReturnCode.NOT_FOUND, "find $req"
            )
        )
    }

    data class REQ<PROJ>(
        val payload: CustomerModel.GetCustomerREQ,

        override val projection: Class<PROJ>
    ) : BizLogicModel.REQ.Data<PROJ>(projection)

    data class RES<T>(
        override val data: T
    ) : BizLogicModel.RES.Single<T>(data)
}