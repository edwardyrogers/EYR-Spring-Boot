package com.eyr.demo.core.kotlin.constants

import com.eyr.demo.core.kotlin.models.AsccendResponse
import com.fasterxml.jackson.databind.ObjectMapper
import java.text.MessageFormat
import java.util.*

enum class AsccendCode(
    val value: String,
) {
    UNKNOWN(""),
    CARD_NOT_FOUND("1"),
    CARD_EXPIRY_MISMATCH("2"),
    INVALID_CARD_STATUS("3"),
    ACCOUNT_NOT_FOUND("4"),
    CUSTOMER_NOT_FOUND("5"),
    EXPIRED_CARD("6"),
    CARD_ALREADY_ACTIVATED("7"),
    INVALID_CARD_BLOCK_CODE("8"),
    INVALID_PASS("9"),
    INVALID_TEMP_LIMIT("10"),
    TEMP_LIMIT_UPDATE_ERROR("11"),
    INVALID_MOBILE_EMAIL("12"),
    NEW_CARD_REPLACEMENT_FOUND("13"),
    INVALID_ACCOUNT_BLOCK_CODE("14"),
    ACCOUNT_OVER_DELQ("15"),
    ACCOUNT_OVER_LIMIT("16"),
    INVALID_REQUEST_FIELDS("17"),
    NO_TRANSACTIONS_FOUND("18"),
    MINIMUM_AMOUNT_NOT_REACH("19"),
    NO_PLAN_FOUND("20"),
    LAST_PAGE_ALREADY_REACH("21"),
    LAST_RECORD_KEY_IS_BLANK("22"),
    INVALID_FINAL_INDICATOR("23"),
    CARD_ACTIVATION_FAILED("24"),
    CUSTOMER_MOBILE_NUMBER_IS_BLACK_LISTED("25"),
    OTHER_FILE_ERROR("26"),
    CARD_FILE_ERROR("90"),
    CUSTOMER_FILE_ERROR("91"),
    ACCOUNT_FILE_ERROR("92"),
    INSTALMENT_CONVERSION_REQUEST_EXIST("93"),
    SYSTEM_ERROR("99"),
    GOOD_RESPONSE("00");

    companion object {
        private fun fromCode(code: String): AsccendCode? = run {
            values().find { it.value == code }
        }

        fun fromJson(errorMSG: String): AsccendResponse = run {
            val result = ObjectMapper().readValue(
                "{$errorMSG}", Map::class.java
            )

            AsccendResponse(
                code = fromCode(result["code"].toString()) ?: UNKNOWN,
                message = result["message"].toString()
            )
        }

        fun toMessage(vararg params: Any): String = run {
            val resourceBundle = ResourceBundle.getBundle("hsbc_core_messages", Locale.ENGLISH)
            val message = resourceBundle.getString("asccend.error")
            MessageFormat.format(message, *params)
        }
    }
}