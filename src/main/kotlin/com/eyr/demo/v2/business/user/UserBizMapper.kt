package cc.worldline.customermanagement.v2.business.user

import cc.worldline.common.utils.DateUtils
import cc.worldline.customermanagement.v2.api.customer.CustomerModel
import cc.worldline.customermanagement.v2.datasource.user.UserRepoEntity
import org.mapstruct.*
import java.time.LocalDate

@Mapper(componentModel = "spring")
interface UserBizMapper {

    fun toUserRepoEntity(
        user: UserProjector.UserEntity
    ): UserRepoEntity

    @Mappings(
        Mapping(
            target = "customerSince",
            source = "user.customerSince",
            qualifiedByName = ["parseToFront"]
        )
    )
    fun toUserEntity(
        user: UserRepoEntity
    ): UserProjector.UserEntity

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(
        target = "dateOfBirth",
        expression = "java(UserBizMapper.parseDate(updateBody.getDateOfBirth()))"
    )
    @Mapping(
        target = "username",
        expression = "java(updateBody.getUsername() != null ? updateBody.getUsername().toLowerCase() : user.getUsername())"
    )
    @Mapping(
        target = "modifiedDate",
        expression = "java(java.time.LocalDateTime.now())"
    )
    @Mapping(
        target = "emailModifiedDate",
        expression = "java(updateBody.getEmail() != null && !updateBody.getEmail().equals(user.getEmail()) ? java.time.LocalDateTime.now() : user.getEmailModifiedDate())"
    )
    @Mapping(
        target = "phoneModifiedDate",
        expression = "java(updateBody.getPhoneNo() != null && !updateBody.getPhoneNo().equals(user.getPhoneNo()) ? java.time.LocalDateTime.now() : user.getPhoneModifiedDate())"
    )
    @Mapping(
        target = "user.username",
        source = "username"
    )
    @Mapping(
        target = "user.customerNumber",
        source = "customerNumber"
    )
    fun updateUserFromUpdateBody(
        username: String? = null,
        customerNumber: String? = null,
        updateBody: CustomerModel.UpdateCustomerREQ,
        @MappingTarget user: UserProjector.UserEntity
    )

    /// =========================== Data Conversion Methods
    @Named("parseToFront")
    fun parseToFront(date: LocalDate?): String? {
        return DateUtils.asianDateFormatter("-").format(date)
    }

    companion object {
        @JvmStatic
        fun parseDate(dateString: String?): LocalDate = run {
            listOf(
                DateUtils.asianDateFormatter("-"),
                DateUtils.europeDateFormatter("-")
            ).asSequence()
                .mapNotNull { formatter ->
                    runCatching { LocalDate.parse(dateString, formatter) }.getOrNull()
                }
                .firstOrNull() ?: LocalDate.EPOCH
        }
    }
}
