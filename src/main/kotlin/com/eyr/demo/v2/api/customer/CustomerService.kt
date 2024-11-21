package cc.worldline.customermanagement.v2.api.customer

interface CustomerService {
    suspend fun getCustomerByID(
        payload: CustomerModel.GetCustomerREQ.ByID
    ): CustomerModel.GetCustomerRES

    suspend fun getCustomerByIC(
        payload: CustomerModel.GetCustomerREQ.ByIC
    ): CustomerModel.GetCustomerRES

    suspend fun getCustomerByUsername(
        payload: CustomerModel.GetCustomerREQ.ByUsername
    ): CustomerModel.GetCustomerRES

    suspend fun getCustomerByCustomerNumber(
        payload: CustomerModel.GetCustomerREQ.ByCustomerNumber
    ): CustomerModel.GetCustomerRES

    suspend fun getCustomers(
        payload: CustomerModel.GetCustomersREQ.All
    ): CustomerModel.GetCustomersRES

    suspend fun getCustomersByIC(
        payload: CustomerModel.GetCustomersREQ.ByIC
    ): CustomerModel.GetCustomersRES

    suspend fun getCustomersByCustomerNumber(
        payload: CustomerModel.GetCustomersREQ.ByCustomerNumber
    ): CustomerModel.GetCustomersRES

    suspend fun lockCustomer(
        payload: CustomerModel.LockCustomerREQ
    )

    suspend fun updateCustomerByID(
        payload: CustomerModel.UpdateCustomerREQ.ByID
    ): CustomerModel.UpdateCustomerRES

    suspend fun updateCustomerByUsername(
        payload: CustomerModel.UpdateCustomerREQ.ByUsername
    ): CustomerModel.UpdateCustomerRES

    suspend fun updateCustomerByCustomerNumber(
        payload: CustomerModel.UpdateCustomerREQ.ByCustomerNumber
    ): CustomerModel.UpdateCustomerRES

    suspend fun verifyCustomerByPassword(
        payload: CustomerModel.VerifyCustomerREQ.ByPwd
    ): CustomerModel.VerifyCustomerRES

    suspend fun syncCustomerDetailForEmail(
        payload: CustomerModel.SyncCustomerREQ.ForEmail
    ): CustomerModel.SyncCustomerRES

    suspend fun syncCustomerDetailForPhone(
        payload: CustomerModel.SyncCustomerREQ.ForPhone
    ): CustomerModel.SyncCustomerRES
}