package cc.worldline.customermanagement.v2.business.user.bizlogics

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicValueReturn
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.common.util.HashPassword
import cc.worldline.customermanagement.v2.common.projections.ForUserPwd
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component

@Component
class VerifyUserBizLogic(
    private val _repository: UserRepository,
) : BizLogicValueReturn<VerifyUserBizLogic.REQ, VerifyUserBizLogic.RES> {

    override suspend fun execute(req: REQ): BizLogicModel.RES.Single<RES> = run {
        val response = _repository.findByUsernameIgnoreCase(
            req.username, ForUserPwd::class.java
        ).orElse(null)

        val user = response ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        val isPositive = when (req) {
            is REQ.ByPwd -> HashPassword.matchPassword(
                req.pwd, user.password
            )
        }

        BizLogicModel.RES.Single(
            RES(
                isPositive
            )
        )
    }

    sealed class REQ(
        open val username: String,
    ) : BizLogicModel.REQ.ValueReturn() {
        data class ByPwd(
            val pwd: String,

            override val username: String,
        ) : REQ(username)

    }

    data class RES(
        val positive: Boolean
    )
}