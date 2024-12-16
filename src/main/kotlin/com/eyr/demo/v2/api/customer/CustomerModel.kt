package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.common.interfaces.Payload
import cc.worldline.common.models.Paged
import cc.worldline.customermanagement.v2.business.user.UserProjector
import cc.worldline.customermanagement.v2.business.user.bizlogics.UpdateUserBizLogic
import cc.worldline.customermanagement.v2.common.constants.StatementPreference
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import cc.worldline.customermanagement.v2.common.mask.MaskJsonSerialiser
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.math.BigInteger
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class CustomerModel {
    // ============================= GetCustomer
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = GetCustomerREQ.ByID::class,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = GetCustomerREQ.ByID::class, name = "ID"),
        JsonSubTypes.Type(value = GetCustomerREQ.ByIC::class, name = "IC"),
        JsonSubTypes.Type(value = GetCustomerREQ.ByUsername::class, name = "USERNAME"),
        JsonSubTypes.Type(value = GetCustomerREQ.ByCustomerNumber::class, name = "CUSTOMER_NUMBER"),
    )
    sealed class GetCustomerREQ(
        open val proj: String = ""
    ) : Payload {

        data class ByID(
            @field:Positive
            val id: BigInteger,
        ) : GetCustomerREQ()

        data class ByIC(
            @field:NotBlank
            val ic: String,
        ) : GetCustomerREQ()

        data class ByUsername(
            @field:NotBlank
            val username: String,
        ) : GetCustomerREQ()

        data class ByCustomerNumber(
            @field:NotBlank
            val customerNumber: String,
        ) : GetCustomerREQ()
    }

    data class GetCustomerRES(
        @JsonUnwrapped
        val customer: UserProjector
    ) : Payload

    // ============================= GetCustomers
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = GetCustomersREQ.All::class,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = GetCustomersREQ.ByCustomerNumber::class, name = "CUSTOMER_NUMBER"),
        JsonSubTypes.Type(value = GetCustomersREQ.ByIC::class, name = "IC"),
        JsonSubTypes.Type(value = GetCustomersREQ.All::class, name = "ALL"),
    )
    sealed class GetCustomersREQ(
        open val sort: String? = null,

        open val orderBy: String = "createdDate",

        open val pageSize: Int = 20,

        open val pageNumber: Int = 0,

        open val proj: String = ""
    ) : Payload {
        data class ByIC(
            @field: NotBlank
            val customerIC: String,
        ) : GetCustomersREQ()

        data class ByCustomerNumber(
            @field: NotBlank
            val customerNumber: String,
        ) : GetCustomersREQ()

        class All : GetCustomersREQ()
    }

    data class GetCustomersRES(
        val customers: Paged<out UserProjector>
    ) : Payload

    // ============================= LockCustomer
    data class LockCustomerREQ(
        @field:NotBlank
        val username: String
    ) : Payload

    data class LockCustomerRES(
        val hasLocked: Boolean
    ) : Payload

    // ============================= UpdateCustomer
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = UpdateCustomerREQ.ByID::class,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = UpdateCustomerREQ.ByID::class, name = "ID"),
        JsonSubTypes.Type(value = UpdateCustomerREQ.ByUsername::class, name = "USERNAME"),
        JsonSubTypes.Type(value = UpdateCustomerREQ.ByCustomerNumber::class, name = "CUSTOMER_NUMBER"),
    )
    sealed class UpdateCustomerREQ(
        open val username: String? = null,

        open val customerNumber: String? = null,

        open val email: String? = null,

        open val phoneNo: String? = null,

        open val ic: String? = null,

        open val traceId: String? = null,

        open val status: UserStatus? = null,

        open val statementPreference: StatementPreference? = null,

        open val nameEN: String? = null,

        open val nameZH: String? = null,

        open val dateOfBirth: String? = null,

        open val addressLine1: String? = null,

        open val addressLine2: String? = null,

        open val addressLine3: String? = null,

        open val postalCode: String? = null,

        open val city: String? = null,

        open val stateCode: String? = null,

        open val countryCode: String? = null,

        open val cycleDate: String? = null,
    ) : Payload {
        data class ByID(
            @field: Positive
            val id: BigInteger,
        ) : UpdateCustomerREQ()

        data class ByUsername(
            @field: NotBlank
            override val username: String,
        ) : UpdateCustomerREQ()

        data class ByCustomerNumber(
            @field: NotBlank
            override val customerNumber: String,
        ) : UpdateCustomerREQ()
    }

    data class UpdateCustomerRES(
        val updatedUser: UserProjector.UserEntity,
        val updateUserAnalyticData: UpdateUserBizLogic.UpdateUserAnalyticData
    ) : Payload

    // ============================= VerifyCustomer
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = VerifyCustomerREQ.ByPwd::class,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = VerifyCustomerREQ.ByPwd::class, name = "PWD"),
    )
    sealed class VerifyCustomerREQ(
        @field: NotBlank
        open val username: String,
    ) : Payload {
        data class ByPwd(
            @field: NotBlank
            val pwd: String,

            override val username: String,
        ) : VerifyCustomerREQ(username)
    }

    data class VerifyCustomerRES(
        val positive: Boolean
    ) : Payload

    // ============================= SyncUpdate
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = SyncCustomerREQ.ForEmail::class,
        property = "for"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = SyncCustomerREQ.ForEmail::class, name = "EMAIL"),
        JsonSubTypes.Type(value = SyncCustomerREQ.ForPhone::class, name = "PHONE"),
    )
    sealed class SyncCustomerREQ : Payload {
        data class ForEmail(
            @field: NotBlank
            val username: String,
        ) : SyncCustomerREQ()

        data class ForPhone(
            @field: NotBlank
            val username: String,
        ) : SyncCustomerREQ()
    }

    data class SyncCustomerRES(
        val hasChange: Boolean
    ) : Payload
}