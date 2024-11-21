package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.common.interfaces.Payload
import cc.worldline.common.models.Paged
import cc.worldline.customermanagement.database.Gender
import cc.worldline.customermanagement.v2.common.constants.StatementPreference
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class CustomerModel {
    // ============================= GetCustomer
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = GetCustomerREQ.ByID::class, name = "ID"),
        JsonSubTypes.Type(value = GetCustomerREQ.ByIC::class, name = "IC"),
        JsonSubTypes.Type(value = GetCustomerREQ.ByUsername::class, name = "USERNAME"),
        JsonSubTypes.Type(value = GetCustomerREQ.ByCustomerNumber::class, name = "CUSTOMER_NUMBER"),
    )
    sealed class GetCustomerREQ(
        @field:NotNull
        open val projection: Projection
    ) : Payload {
        data class ByID(
            @field:NotBlank
            val id: BigInteger,

            override val projection: Projection
        ) : GetCustomerREQ(projection)

        data class ByIC(
            @field:NotBlank
            val ic: String,

            override val projection: Projection
        ) : GetCustomerREQ(projection)

        data class ByUsername(
            @field:NotBlank
            val username: String,

            override val projection: Projection
        ) : GetCustomerREQ(projection)

        data class ByCustomerNumber(
            @field:NotBlank
            val customerNumber: String,

            override val projection: Projection
        ) : GetCustomerREQ(projection)

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        enum class Projection {
            FOR_LOGIN,
        }
    }

    data class GetCustomerRES(
        val id: BigInteger,

        val username: String = "",

        val email: String?,

        val phoneNo: String?,

        val ic: String = "",

        val createdDate: LocalDateTime?,

        val modifiedDate: LocalDateTime?,

        val traceId: String?,

        val status: UserStatus,

        val statementPreference: StatementPreference?,

        val nameEN: String?,

        val nameZH: String,

        val dateOfBirth: LocalDate,

        val addressLine1: String,

        val addressLine2: String?,

        val addressLine3: String?,

        val postalCode: String,

        val city: String,

        val stateCode: String,

        val countryCode: String,

        val customerNumber: String,

        val cycleDate: String,

        val gender: Gender,

        val customerSince: String?,

        val languagePreference: String?,

        val phoneModifiedDate: LocalDateTime?,

        val emailModifiedDate: LocalDateTime?,

        val passwordModifiedDate: LocalDateTime?,
    ) : Payload

    // ============================= GetCustomers
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = GetCustomersREQ.ByCustomerNumber::class, name = "CUSTOMER_NUMBER"),
        JsonSubTypes.Type(value = GetCustomersREQ.ByIC::class, name = "IC"),
        JsonSubTypes.Type(value = GetCustomersREQ.All::class, name = "ALL"),
    )
    sealed class GetCustomersREQ(
        @field:NotNull
        open val projection: Projection,

        open val sort: String? = null,

        open val orderBy: String = "createdDate",

        open val pageSize: Int = 20,

        open val pageNumber: Int = 0,
    ) : Payload {
        data class ByIC(
            @field: NotBlank
            val customerIC: String,

            override val projection: Projection,
        ) : GetCustomersREQ(projection)

        data class ByCustomerNumber(
            @field: NotBlank
            val customerNumber: String,

            override val projection: Projection,
        ) : GetCustomersREQ(projection)

        data class All(
            override val projection: Projection,
        ) : GetCustomersREQ(projection)

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        enum class Projection {
            FOR_GENERAL,
        }
    }

    data class GetCustomersRES(
        val customers: Paged<GetCustomerRES>
    ) : Payload

    // ============================= LockCustomer
    data class LockCustomerREQ(
        @field:NotBlank
        val username: String
    ) : Payload

    // ============================= UpdateCustomer
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "by"
    )
    @JsonSubTypes(
        JsonSubTypes.Type(value = UpdateCustomerREQ.ByID::class, name = "ID"),
        JsonSubTypes.Type(value = UpdateCustomerREQ.ByUsername::class, name = "USERNAME"),
        JsonSubTypes.Type(value = UpdateCustomerREQ.ByCustomerNumber::class, name = "CUSTOMER_NUMBER"),
    )
    sealed class UpdateCustomerREQ(
        open var email: String? = null,

        open var phoneNo: String? = null,

        open var ic: String? = null,

        open var traceId: String? = null,

        open var status: UserStatus? = null,

        open var statementPreference: StatementPreference? = null,

        open var nameEN: String? = null,

        open var nameZH: String? = null,

        open var dateOfBirth: String? = null,

        open var addressLine1: String? = null,

        open var addressLine2: String? = null,

        open var addressLine3: String? = null,

        open var postalCode: String? = null,

        open var city: String? = null,

        open var stateCode: String? = null,

        open var countryCode: String? = null,

        open var cycleDate: String? = null
    ) : Payload {
        data class ByID(
            @field: NotBlank
            val id: BigInteger,

            val username: String? = null,
            val customerNumber: String? = null,
        ) : UpdateCustomerREQ()

        data class ByUsername(
            @field: NotBlank
            val username: String,

            val customerNumber: String? = null,
        ) : UpdateCustomerREQ()

        data class ByCustomerNumber(
            @field: NotBlank
            val customerNumber: String,

            val username: String? = null
        ) : UpdateCustomerREQ()
    }

    data class UpdateCustomerRES(
        val originalData: String,
        val updatedData: String
    ) : Payload

    // ============================= VerifyCustomer
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
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