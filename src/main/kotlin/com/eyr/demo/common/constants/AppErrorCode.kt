package com.eyr.demo.common.constants

enum class AppErrorCode(
    val code: String,
    val msg: String,
) {
    VALIDATION_FAILED("0001", "Validation failed"),
    BODY_VALIDATION_FAILED("0002", "Please, check your body parameters"),
    UNKNOWN_REASON("9999", "Unknown reason");
}

