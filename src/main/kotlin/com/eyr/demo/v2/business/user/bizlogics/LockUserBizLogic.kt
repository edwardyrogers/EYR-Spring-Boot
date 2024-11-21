package cc.worldline.customermanagement.v2.business.user.bizlogics

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicVoid
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.business.user.UserEntity
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component

@Component
class LockUserBizLogic(
    private val _repository: UserRepository
) : BizLogicVoid<LockUserBizLogic.REQ> {

    override suspend fun execute(req: REQ) = run {
        val response = _repository.findByUsernameIgnoreCase(
            req.username, UserEntity::class.java
        ).orElse(null)

        val entity = response ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        val newUser = entity.copy(status = UserStatus.LOCKED)

        _repository.save(newUser)

        Unit
    }


    data class REQ(
        val username: String
    ) : BizLogicModel.REQ.Void()
}