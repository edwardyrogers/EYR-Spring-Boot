package com.eyr.demo.common.exceptions

import com.eyr.demo.common.constants.AppErrorCode


class RequestFailedException(
    val code: AppErrorCode,
    val msg: String?,
) : RuntimeException(msg)