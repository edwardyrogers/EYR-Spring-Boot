package cc.worldline.customermanagement.v2.business.user.bizlogics

import com.eyr.demo.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicValueReturn
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.api.customer.CustomerModel
import cc.worldline.customermanagement.v2.business.user.UserBizMapper
import cc.worldline.customermanagement.v2.business.user.UserProjector
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateUserBizLogic(
    private val _mapper: UserBizMapper,
    private val _repository: UserRepository,
    private val _objectMapper: ObjectMapper,
) : BizLogicValueReturn<UpdateUserBizLogic.REQ, UpdateUserBizLogic.RES> {

    override fun execute(req: REQ): BizLogicModel.RES.Single<RES> = run {
        val response = when (val payload = req.payload) {

            is CustomerModel.UpdateCustomerREQ.ByID -> _repository.findById(
                payload.id, UserProjector.UserEntity::class.java
            ).let {
                doUpdateToUser(payload.username, payload.customerNumber, payload, it)
            }

            is CustomerModel.UpdateCustomerREQ.ByUsername -> _repository.findByUsernameIgnoreCase(
                payload.username, UserProjector.UserEntity::class.java
            ).let {
                doUpdateToUser(payload.username, payload.customerNumber, payload, it)
            }

            is CustomerModel.UpdateCustomerREQ.ByCustomerNumber -> _repository.findByCustomerNumber(
                payload.customerNumber, UserProjector.UserEntity::class.java
            ).let {
                doUpdateToUser(payload.username, payload.customerNumber, payload, it)
            }
        }

        BizLogicModel.RES.Single(
            data = response
        )
    }

    fun doUpdateToUser(
        username: String? = null,
        customerNumber: String? = null,
        payload: CustomerModel.UpdateCustomerREQ,
        user: Optional<UserProjector.UserEntity>
    ) = run {
        if (user.isEmpty) {
            throw ServiceException(
                ReturnCode.NOT_FOUND, "find $payload"
            )
        }

        val userEntity = user.get()

        val originalDataStringify = _objectMapper.writeValueAsString(userEntity)

        _mapper.updateUserFromUpdateBody(username, customerNumber, payload, userEntity)

        val updatedDataStringify = _objectMapper.writeValueAsString(userEntity)

        // the modified date change on second so there is no need to check
        // if data has changed then perform modification
        val updatedCustomer = _repository.save(
            _mapper.toUserRepoEntity(userEntity)
        )

        RES(
            updatedUser = _mapper.toUserEntity(updatedCustomer),
            updateUserAnalyticData = UpdateUserAnalyticData(
                originalData = originalDataStringify,
                updatedData = updatedDataStringify
            )
        )
    }

    data class REQ(
        val payload: CustomerModel.UpdateCustomerREQ,
    ) : BizLogicModel.REQ.ValueReturn()

    data class RES(
        val updatedUser: UserProjector.UserEntity,
        val updateUserAnalyticData: UpdateUserAnalyticData
    )

    data class UpdateUserAnalyticData(
        val originalData: String,
        val updatedData: String
    )
}