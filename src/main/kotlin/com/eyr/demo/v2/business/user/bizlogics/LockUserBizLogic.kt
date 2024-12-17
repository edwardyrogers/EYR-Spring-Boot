package cc.worldline.customermanagement.v2.business.user.bizlogics

import com.eyr.demo.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicValueReturn
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.business.user.UserBizMapper
import cc.worldline.customermanagement.v2.business.user.UserProjector
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import org.springframework.stereotype.Component

@Component
class LockUserBizLogic(
    private val _mapper: UserBizMapper,
    private val _repository: UserRepository
) : BizLogicValueReturn<LockUserBizLogic.REQ, LockUserBizLogic.RES> {

    override fun execute(req: REQ): BizLogicModel.RES.Single<RES> = run {
        val response = _repository.findByUsernameIgnoreCase(
            req.username, UserProjector.UserEntity::class.java
        ).orElse(null)

        val entity = response ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        val newUser = entity.copy(status = UserStatus.LOCKED)

        val user = _repository.save(
            _mapper.toUserRepoEntity(newUser)
        )

        return@run BizLogicModel.RES.Single(
            RES(
                hasLocked = user.status == UserStatus.LOCKED
            )
        )
    }

    data class REQ(
        val username: String
    ) : BizLogicModel.REQ.ValueReturn()

    data class RES(
        val hasLocked: Boolean
    ) : BizLogicModel.REQ.Void()
}