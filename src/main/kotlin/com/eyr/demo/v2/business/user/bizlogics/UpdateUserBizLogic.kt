package cc.worldline.customermanagement.v2.business.user.bizlogics

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
import cc.worldline.common.interfaces.BizLogicValueReturn
import cc.worldline.common.models.BizLogicModel
import cc.worldline.customermanagement.v2.business.user.UserEntity
import cc.worldline.customermanagement.v2.business.user.UserMapper
import cc.worldline.customermanagement.v2.common.constants.StatementPreference
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import cc.worldline.customermanagement.v2.datasource.user.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class UpdateUserBizLogic(
    private val _mapper: UserMapper,
    private val _repository: UserRepository,
    private val _objectMapper: ObjectMapper,
) : BizLogicValueReturn<UpdateUserBizLogic.REQ, UpdateUserBizLogic.RES> {

    override suspend fun execute(req: REQ): BizLogicModel.RES.Single<RES> = run {
        val response = when (req) {
            is REQ.ByID -> _repository.findById(
                req.id, UserEntity::class.java
            ).orElse(null)

            is REQ.ByUsername -> _repository.findByUsernameIgnoreCase(
                req.username, UserEntity::class.java
            ).orElse(null)

            is REQ.ByCustomerNumber -> _repository.findByCustomerNumber(
                req.customerNumber, UserEntity::class.java
            ).orElse(null)
        }

        val user = response ?: throw ServiceException(
            ReturnCode.NOT_FOUND, "find $req"
        )

        val originalDataStringify = _objectMapper.writeValueAsString(user)

        _mapper.updateCustomerFromUpdateBody(req.data, user)

        val updatedDataStringify = _objectMapper.writeValueAsString(user)

        // the modified date change on second so there is no need to check
        // if data has changed then perform modification
        _repository.save(user)

        BizLogicModel.RES.Single(
            data = RES(
                originalDataStringify,
                updatedDataStringify
            )
        )
    }

    sealed class REQ(
        val data: UpdateData
    ) : BizLogicModel.REQ.ValueReturn() {
        class ByID(
            val id: BigInteger,
            data: UpdateData
        ) : REQ(data)

        class ByUsername(
            val username: String,
            data: UpdateData
        ) : REQ(data)

        class ByCustomerNumber(
            val customerNumber: String,
            data: UpdateData
        ) : REQ(data)

        data class UpdateData(
            val username: String? = null,

            val customerNumber: String? = null,

            var email: String? = null,

            var phoneNo: String? = null,

            var ic: String? = null,

            var traceId: String? = null,

            var status: UserStatus? = null,

            var statementPreference: StatementPreference? = null,

            var nameEN: String? = null,

            var nameZH: String? = null,

            var dateOfBirth: String? = null,

            var addressLine1: String? = null,

            var addressLine2: String? = null,

            var addressLine3: String? = null,

            var postalCode: String? = null,

            var city: String? = null,

            var stateCode: String? = null,

            var countryCode: String? = null,

            var cycleDate: String? = null
        )
    }

    data class RES(
        val originalData: String,
        val updatedData: String
    )
}