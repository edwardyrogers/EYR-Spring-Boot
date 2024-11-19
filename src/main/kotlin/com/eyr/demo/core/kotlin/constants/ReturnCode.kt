package cc.worldline.common.constants

import cc.worldline.common.interfaces.Code
import java.text.MessageFormat
import java.util.*

/**
 * Enum class representing various return codes for error handling.
 *
 * Each return code consists of a unique value and an associated message
 * that describes the meaning of the code.
 *
 * @property value A string representing the unique code for the error.
 * @property msgKey A string used to retrieve the descriptive message for the error from resource bundles.
 */
enum class ReturnCode(
    override val value: String,
    override val msgKey: String
) : Code {
    UNEXPECTED_ERROR("000", "unexpected.error"),
    GENERAL_ERROR("001", "general.error"),
    ACCESS_DENIED("002", "access.denied"),
    ALREADY_EXIST("003", "already.exist"),
    NOT_FOUND("004", "not.found"),
    MANDATORY_FIELD("005", "mandatory.field"),
    INVALID("006", "invalid"),
    DATE_FORMAT_INCORRECT("007", "date.format.incorrect"),
    MAPPER_NOT_MATCHED("008", "mapper.not.matched"),
    PROJECTION_NOT_MATCHED("009", "projection.not.matched"),
    ASCCEND_ERROR("010", "asccend.error");

    /**
     * Retrieves the message associated with the error code based on the provided locale.
     *
     * If no locale is specified, it defaults to [Locale.ENGLISH].
     *
     * @param locale The locale for which to retrieve the message.
     * @return The localized message corresponding to the error code.
     */
    override fun messageIn(locale: Locale, vararg params: Any): String = run {
        val resourceBundle = ResourceBundle.getBundle("hsbc_core_messages", locale)
        val message = resourceBundle.getString(msgKey)
        MessageFormat.format(message, *params)
    }
}