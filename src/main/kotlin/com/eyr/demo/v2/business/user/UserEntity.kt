package cc.worldline.customermanagement.v2.business.user

import cc.worldline.customermanagement.database.Gender
import cc.worldline.customermanagement.v2.common.constants.StatementPreference
import cc.worldline.customermanagement.v2.common.constants.UserStatus
import java.io.Serializable
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: BigInteger = BigInteger.valueOf(-9999999),

    @Column(name = "username")
    var username: String,

    @Column(name = "password")
    var password: String,

    @Column(name = "email")
    var email: String?,

    @Column(name = "phone_no")
    var phoneNo: String?,

    @Column(name = "ic")
    var ic: String,

    @Column(name = "trace_id")
    var traceId: String?,

    @Column(name = "created_date")
    var createdDate: LocalDateTime?,

    @Column(name = "modified_date")
    var modifiedDate: LocalDateTime?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: UserStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "statement_preference")
    var statementPreference: StatementPreference?,

    @Column(name = "name_en")
    var nameEN: String?,

    @Column(name = "name_zh")
    var nameZH: String,

    @Column(name = "date_of_birth")
    var dateOfBirth: LocalDate,

    @Column(name = "address_line1")
    var addressLine1: String,

    @Column(name = "address_line2")
    var addressLine2: String?,

    @Column(name = "address_line3")
    var addressLine3: String?,

    @Column(name = "postal_code")
    var postalCode: String,

    @Column(name = "city")
    var city: String,

    @Column(name = "state_code")
    var stateCode: String,

    @Column(name = "country_code")
    var countryCode: String,

    @Column(name = "customer_number")
    var customerNumber: String,

    @Column(name = "cycle_date")
    var cycleDate: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    var gender: Gender,

    @Column(name = "customer_since")
    var customerSince: LocalDate?,

    @Column(name = "created_by")
    var createdBy: String,

    @Column(name = "modified_by")
    var modifiedBy: String,

    @Column(name = "phone_modified_date")
    var phoneModifiedDate: LocalDateTime?,

    @Column(name = "email_modified_date")
    var emailModifiedDate: LocalDateTime?,

    @Column(name = "language_preference")
    var languagePreference: String?,

    @Column(name = "password_modified_date")
    var passwordModifiedDate: LocalDateTime?,
) : Serializable
