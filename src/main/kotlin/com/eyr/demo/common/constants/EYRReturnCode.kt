package com.eyr.demo.common.constants

import com.eyr.demo.api.user.UserController
import com.eyr.demo.core.interfaces.Code
import java.text.MessageFormat
import java.util.*

enum class EYRReturnCode(
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
                UserController::class -> "01"
                else -> "00"
            }
        }
    }
}