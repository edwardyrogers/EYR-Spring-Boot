package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.common.models.Paged
import cc.worldline.customermanagement.v2.api.customer.CustomerModel.GetCustomerREQ
import cc.worldline.customermanagement.v2.api.customer.CustomerModel.GetCustomersREQ
import cc.worldline.customermanagement.v2.business.user.UserEntity
import cc.worldline.customermanagement.v2.business.user.bizlogics.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val _mapper: CustomerMapper,
    private val _getUsersBizLogic: GetUsersBizLogic,
    private val _getUserBizLogic: GetUserBizLogic,
    private val _lockUserBizLogic: LockUserBizLogic,
    private val _updateUserBizLogic: UpdateUserBizLogic,
    private val _syncUserBizLogic: SyncUserBizLogic,
    private val _verifyBizLogic: VerifyUserBizLogic,
) : CustomerService {
    override suspend fun getCustomerByID(
        payload: GetCustomerREQ.ByID
    ): CustomerModel.GetCustomerRES = run {
        val projection = when (payload.projection) {
            GetCustomerREQ.Projection.FOR_LOGIN -> UserEntity::class.java
        }

        val response = _getUserBizLogic.execute(
            GetUserBizLogic.REQ.ByID(
                payload.id, projection
            )
        )

        when (val data = response.data) {
            else -> _mapper.toGetCustomerRES(data)
        }
    }

    override suspend fun getCustomerByIC(
        payload: GetCustomerREQ.ByIC
    ): CustomerModel.GetCustomerRES = run {
        val projection = when (payload.projection) {
            GetCustomerREQ.Projection.FOR_LOGIN -> UserEntity::class.java
        }

        val response = _getUserBizLogic.execute(
            GetUserBizLogic.REQ.ByIC(
                payload.ic, projection
            )
        )

        when (val data = response.data) {
            else -> _mapper.toGetCustomerRES(data)
        }
    }

    override suspend fun getCustomerByUsername(
        payload: GetCustomerREQ.ByUsername
    ): CustomerModel.GetCustomerRES = run {
        val projection = when (payload.projection) {
            GetCustomerREQ.Projection.FOR_LOGIN -> UserEntity::class.java
        }

        val response = _getUserBizLogic.execute(
            GetUserBizLogic.REQ.ByUsername(
                payload.username, projection
            )
        )

        when (val data = response.data) {
            else -> _mapper.toGetCustomerRES(data)
        }
    }

    override suspend fun getCustomerByCustomerNumber(
        payload: GetCustomerREQ.ByCustomerNumber
    ): CustomerModel.GetCustomerRES = run {
        val projection = when (payload.projection) {
            GetCustomerREQ.Projection.FOR_LOGIN -> UserEntity::class.java
        }

        val response = _getUserBizLogic.execute(
            GetUserBizLogic.REQ.ByCustomerNumber(
                payload.customerNumber, projection
            )
        )

        when (val data = response.data) {
            else -> _mapper.toGetCustomerRES(data)
        }
    }

    override suspend fun getCustomers(payload: GetCustomersREQ.All): CustomerModel.GetCustomersRES = run {
        val projection = when (payload.projection) {
            GetCustomersREQ.Projection.FOR_GENERAL -> UserEntity::class.java
        }

        val response = _getUsersBizLogic.execute(
            GetUsersBizLogic.REQ.All(
                getPageRequest(
                    payload.sort, payload.orderBy, payload.pageSize, payload.pageNumber,
                ),
                projection
            )
        ).data.map {
            _mapper.toGetCustomerRES(it)
        }


        when (val data = Paged(response)) {
            else -> CustomerModel.GetCustomersRES(customers = data)
        }
    }

    override suspend fun getCustomersByIC(
        payload: GetCustomersREQ.ByIC
    ): CustomerModel.GetCustomersRES = run {
        val projection = when (payload.projection) {
            GetCustomersREQ.Projection.FOR_GENERAL -> UserEntity::class.java
        }

        val response = _getUsersBizLogic.execute(
            GetUsersBizLogic.REQ.ByIC(
                payload.customerIC,
                getPageRequest(
                    payload.sort, payload.orderBy, payload.pageSize, payload.pageNumber,
                ),
                projection
            )
        ).data.map {
            _mapper.toGetCustomerRES(it)
        }

        when (val data = Paged(response)) {
            else -> CustomerModel.GetCustomersRES(customers = data)
        }
    }


    override suspend fun getCustomersByCustomerNumber(
        payload: GetCustomersREQ.ByCustomerNumber
    ): CustomerModel.GetCustomersRES = run {
        val projection = when (payload.projection) {
            GetCustomersREQ.Projection.FOR_GENERAL -> UserEntity::class.java
        }

        val response = _getUsersBizLogic.execute(
            GetUsersBizLogic.REQ.ByCustomerNumber(
                payload.customerNumber,
                getPageRequest(
                    payload.sort, payload.orderBy, payload.pageSize, payload.pageNumber,
                ),
                projection
            )
        ).data.map {
            _mapper.toGetCustomerRES(it)
        }

        when (val data = Paged(response)) {
            else -> CustomerModel.GetCustomersRES(customers = data)
        }
    }

    override suspend fun lockCustomer(
        payload: CustomerModel.LockCustomerREQ
    ): Unit = run {
        _lockUserBizLogic.execute(
            LockUserBizLogic.REQ(payload.username)
        )
    }

    override suspend fun updateCustomerByID(
        payload: CustomerModel.UpdateCustomerREQ.ByID
    ): CustomerModel.UpdateCustomerRES = run {
        _updateUserBizLogic.execute(
            UpdateUserBizLogic.REQ.ByID(
                id = payload.id,
                data = _mapper.toCustomerUpdateDataByID(payload)
            )
        ).let {
            _mapper.toUpdateCustomerRES(it.data)
        }
    }

    override suspend fun updateCustomerByUsername(
        payload: CustomerModel.UpdateCustomerREQ.ByUsername
    ): CustomerModel.UpdateCustomerRES = run {
        _updateUserBizLogic.execute(
            UpdateUserBizLogic.REQ.ByUsername(
                username = payload.username,
                data = _mapper.toCustomerUpdateDataByUsername(payload)
            )
        ).let {
            _mapper.toUpdateCustomerRES(it.data)
        }
    }

    override suspend fun updateCustomerByCustomerNumber(
        payload: CustomerModel.UpdateCustomerREQ.ByCustomerNumber
    ): CustomerModel.UpdateCustomerRES = run {
        _updateUserBizLogic.execute(
            UpdateUserBizLogic.REQ.ByCustomerNumber(
                customerNumber = payload.customerNumber,
                data = _mapper.toCustomerUpdateDataByCustomerNumber(payload)
            )
        ).let {
            _mapper.toUpdateCustomerRES(it.data)
        }
    }

    override suspend fun verifyCustomerByPassword(
        payload: CustomerModel.VerifyCustomerREQ.ByPwd
    ): CustomerModel.VerifyCustomerRES = run {
        _verifyBizLogic.execute(
            VerifyUserBizLogic.REQ.ByPwd(
                username = payload.username,
                pwd = payload.pwd
            )
        ).let {
            _mapper.toVerifyCustomerRES(it.data)
        }
    }

    override suspend fun syncCustomerDetailForEmail(
        payload: CustomerModel.SyncCustomerREQ.ForEmail
    ): CustomerModel.SyncCustomerRES = run {
        _syncUserBizLogic.execute(
            SyncUserBizLogic.REQ.ForEmail(
                username = payload.username
            )
        ).let {
            CustomerModel.SyncCustomerRES(
                hasChange = it.data.isInOtpLimited
            )
        }
    }

    override suspend fun syncCustomerDetailForPhone(
        payload: CustomerModel.SyncCustomerREQ.ForPhone
    ): CustomerModel.SyncCustomerRES = run {
        _syncUserBizLogic.execute(
            SyncUserBizLogic.REQ.ForPhone(
                username = payload.username
            )
        ).let {
            CustomerModel.SyncCustomerRES(
                hasChange = it.data.isInOtpLimited
            )
        }
    }

    fun getPageRequest(
        sort: String? = null,
        orderBy: String = "createdDate",
        pageSize: Int = 20,
        pageNumber: Int = 0,
    ) = run {
        PageRequest.of(
            pageNumber,
            pageSize,
            Sort.by(
                if (
                    sort.isNullOrBlank() ||
                    sort.equals("ASC", ignoreCase = true)
                ) {
                    Sort.Direction.ASC
                } else {
                    Sort.Direction.DESC
                },
                orderBy
            )
        )
    }
}