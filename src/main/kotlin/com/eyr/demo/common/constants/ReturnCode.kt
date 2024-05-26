package com.eyr.demo.common.constants

enum class ReturnCode(
    val code: String,
    val msg: String,
) {
    ACCESS_DENIED("0001", "Access denied"),
    VALIDATION_FAILED("0002", "Validation failed"),
    BODY_VALIDATION_FAILED("0003", "Please, check your body parameters"),
    CANNOT_DESERIALIZE_VALUE("0004", "Please, check your field type"),
    CONTENT_NOT_FOUND("0005", "Cannot find what you request"),
    GENERAL_ERROR("9999", "Unknown reason");
}

