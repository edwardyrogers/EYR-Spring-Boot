package cc.worldline.customermanagement.v2.business.user.bizlogics

import com.eyr.demo.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicValueReturn
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.common.util.HashPassword
import cc.worldline.customermanagement.v2.api.customer.CustomerModel
import cc.worldline.customermanagement.v2.business.user.UserProjector
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component

@Component
class VerifyUserBizLogic(
    private val _repository: UserRepository,
) : BizLogicValueReturn<VerifyUserBizLogic.REQ, VerifyUserBizLogic.RES> {

    override fun execute(req: REQ): BizLogicModel.RES.Single<RES> = run {
        val response = _repository.findByUsernameIgnoreCase(
            req.payload.username, UserProjector.UserForPwd::class.java
        ).orElse(null)

        val user = response ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        val isPositive = when (val payload = req.payload) {
            is CustomerModel.VerifyCustomerREQ.ByPwd -> HashPassword.matchPassword(
                payload.pwd, user.password
            )
        }

        BizLogicModel.RES.Single(
            RES(
                isPositive
            )
        )
    }

    class REQ(
        val payload: CustomerModel.VerifyCustomerREQ
    ) : BizLogicModel.REQ.ValueReturn()

    data class RES(
        val positive: Boolean
    )
}