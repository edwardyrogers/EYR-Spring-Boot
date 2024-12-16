package cc.worldline.customermanagement.v2.api.customer

import cc.worldline.customermanagement.v2.business.user.bizlogics.LockUserBizLogic
import cc.worldline.customermanagement.v2.business.user.bizlogics.UpdateUserBizLogic
import cc.worldline.customermanagement.v2.business.user.bizlogics.VerifyUserBizLogic
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CustomerMapper {
    fun toLockedCustomerRES(
        data: LockUserBizLogic.RES
    ): CustomerModel.LockCustomerRES

    fun toUpdateCustomerRES(
        data: UpdateUserBizLogic.RES
    ): CustomerModel.UpdateCustomerRES

    //
    fun toVerifyCustomerRES(
        data: VerifyUserBizLogic.RES
    ): CustomerModel.VerifyCustomerRES
}