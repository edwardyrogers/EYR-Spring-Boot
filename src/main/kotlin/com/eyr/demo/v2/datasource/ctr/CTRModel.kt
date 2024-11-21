package cc.worldline.customermanagement.v2.datasource.ctr

class CTRModel {
    data class CustomerProfileRES(
        val asccendServiceCode: String,
        val uuid: String,
        val responseCode: String,
        val responseDesc: String,
        val responseDateTime: String,
        val emailAdd: String?,
        val mobileNumber: String?,
        val modifiedEmailDate: String?,
        val modifiedMobileDate: String?,
    )
}