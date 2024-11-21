package cc.worldline.customermanagement.v2.business.user

import cc.worldline.common.utils.DateUtils
import cc.worldline.customermanagement.v2.business.user.bizlogics.UpdateUserBizLogic
import org.mapstruct.*
import java.time.LocalDate

@Mapper(componentModel = "spring")
interface UserMapper {
    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(
        target = "dateOfBirth",
        expression = "java(UserMapper.parseDate(updateBody.getDateOfBirth()))"
    )
    @Mapping(
        target = "username",
        expression = "java(updateBody.getUsername() != null ? updateBody.getUsername().toLowerCase() : customer.getUsername())"
    )
    @Mapping(
        target = "modifiedDate",
        expression = "java(java.time.LocalDateTime.now())"
    )
    @Mapping(
        target = "emailModifiedDate",
        expression = "java(updateBody.getEmail() != null && !updateBody.getEmail().equals(customer.getEmail()) ? java.time.LocalDateTime.now() : customer.getEmailModifiedDate())"
    )
    @Mapping(
        target = "phoneModifiedDate",
        expression = "java(updateBody.getPhoneNo() != null && !updateBody.getPhoneNo().equals(customer.getPhoneNo()) ? java.time.LocalDateTime.now() : customer.getPhoneModifiedDate())"
    )
    fun updateCustomerFromUpdateBody(
        updateBody: UpdateUserBizLogic.REQ.UpdateData,
        @MappingTarget customer: UserEntity
    )

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
