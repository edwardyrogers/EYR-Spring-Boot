package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.common.utils.DateUtils
import cc.worldline.customermanagement.v2.business.user.UserEntity
import cc.worldline.customermanagement.v2.business.user.bizlogics.UpdateUserBizLogic
import cc.worldline.customermanagement.v2.business.user.bizlogics.VerifyUserBizLogic
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.Named
import java.time.LocalDate

@Mapper(componentModel = "spring")
interface CustomerMapper {
    /// =========================== Data Mapping Methods
    @Mappings(
        Mapping(
            target = "customerSince",
            source = "user.customerSince",
            qualifiedByName = ["parseToFront"]
        )
    )
    fun toGetCustomerRES(
        user: UserEntity,
    ): CustomerModel.GetCustomerRES

    fun toCustomerUpdateDataByID(
        data: CustomerModel.UpdateCustomerREQ.ByID
    ): UpdateUserBizLogic.REQ.UpdateData

    fun toCustomerUpdateDataByUsername(
        data: CustomerModel.UpdateCustomerREQ.ByUsername
    ): UpdateUserBizLogic.REQ.UpdateData

    fun toCustomerUpdateDataByCustomerNumber(
        data: CustomerModel.UpdateCustomerREQ.ByCustomerNumber
    ): UpdateUserBizLogic.REQ.UpdateData

    fun toUpdateCustomerRES(
        data: UpdateUserBizLogic.RES
    ): CustomerModel.UpdateCustomerRES

    fun toVerifyCustomerRES(
        data: VerifyUserBizLogic.RES
    ): CustomerModel.VerifyCustomerRES

    /// =========================== Data Conversion Methods
    @Named("parseToFront")
    fun parseToFront(date: LocalDate?): String? {
        return DateUtils.asianDateFormatter("-").format(date)
    }
}