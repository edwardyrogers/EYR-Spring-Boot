package cc.worldline.customermanagement.v2.business.user.bizlogics

import com.eyr.demo.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicFetchMany
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.api.customer.CustomerModel
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class GetUsersBizLogic(
    private val _repository: UserRepository
) : BizLogicFetchMany {
    override fun <T> execute(req: BizLogicModel.REQ.Data<T>): BizLogicModel.RES.Paginated<T> = run {
        if (req !is REQ) throw ServiceException(
            ReturnCode.INVALID, "${req::class.simpleName} Request Model"
        )

        val response = when (val payload = req.payload) {
            is CustomerModel.GetCustomersREQ.ByCustomerNumber -> _repository.findAllByCustomerNumber(
                req.pageable, payload.customerNumber, req.projection
            )


            is CustomerModel.GetCustomersREQ.ByIC -> _repository.findAllByIc(
                req.pageable, payload.customerIC, req.projection
            )

            is CustomerModel.GetCustomersREQ.All -> _repository.findAllBy(
                req.pageable, req.projection
            )
        }

        val customers = response.takeIf { it.content.isNotEmpty() } ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        return@run RES(
            data = customers
        )
    }

    data class REQ<PROJ>(
        val pageable: Pageable,
        val payload: CustomerModel.GetCustomersREQ,

        override val projection: Class<PROJ>
    ) : BizLogicModel.REQ.Data<PROJ>(projection)

    data class RES<T>(
        override val data: Page<T>
    ) : BizLogicModel.RES.Paginated<T>(data)
}