package cc.worldline.customermanagement.v2.common.constants

import cc.worldline.common.interfaces.Code
import cc.worldline.customermanagement.v2.api.customer.CustomerController
import java.text.MessageFormat
import java.util.*

enum class CSMReturnCode(
    override val value: String,
    override val msgKey: String
) : Code {
    ;

    override fun messageIn(locale: Locale, vararg params: Any): String = run {
        val resourceBundle = ResourceBundle.getBundle("errormessages", locale)
        val message = resourceBundle.getString(msgKey)
        MessageFormat.format(message, *params)
    }

    companion object {
        fun findControllerCode(controller: Any): String = run {
            when (controller) {
                CustomerController::class -> "01"
                else -> "00"
            }
        }
    }
}