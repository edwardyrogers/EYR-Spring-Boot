package com.eyr.demo.core.constants

import com.eyr.demo.core.interfaces.Code

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
    ASCCEND_ERROR("010", "asccend.error"),
    ENDPOINT_NOT_FOUND("011", "endpoint.not.found");
}