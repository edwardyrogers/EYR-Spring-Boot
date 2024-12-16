package cc.worldline.customermanagement.v2.business.user

import cc.worldline.customermanagement.database.Gender
import cc.worldline.customermanagement.v2.common.constants.StatementPreference
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import cc.worldline.customermanagement.v2.common.mask.MaskJsonSerialiser
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime

sealed class UserProjector {
    companion object {
        fun fromString(name: String): Class<out UserProjector> {
            return when (name) {
                "LOGIN" -> UserForLogin::class.java
                "PASSWORD" -> UserForPwd::class.java
                else -> UserEntity::class.java
            }
        }
    }

    data class UserEntity(
        var id: BigInteger = BigInteger.valueOf(-9999999),
        var username: String,
        var password: String,
        var email: String?,
        var phoneNo: String?,
        var ic: String,
        var traceId: String?,
        var createdDate: LocalDateTime?,
        var modifiedDate: LocalDateTime?,
        var status: UserStatus,
        var statementPreference: StatementPreference?,
        var nameEN: String?,
        var nameZH: String,
        var dateOfBirth: LocalDate,
        var addressLine1: String,
        var addressLine2: String?,
        var addressLine3: String?,
        var postalCode: String,
        var city: String,
        var stateCode: String,
        var countryCode: String,
        var customerNumber: String,
        var cycleDate: String,
        var gender: Gender,
        var customerSince: LocalDate?,
        var createdBy: String,
        var modifiedBy: String,
        var phoneModifiedDate: LocalDateTime?,
        var emailModifiedDate: LocalDateTime?,
        var languagePreference: String?,
        var passwordModifiedDate: LocalDateTime?,
    ) : UserProjector()

    data class UserForLogin(
        val id: BigInteger,
        @field:JsonSerialize(using = MaskJsonSerialiser::class)
        val username: String,
        val email: String?,
        val phoneNo: String?,
        val ic: String,
        val traceId: String?,
        val status: UserStatus,
        val customerNumber: String,
    ) : UserProjector()

    data class UserForPwd(
        val password: String
    ) : UserProjector()
}