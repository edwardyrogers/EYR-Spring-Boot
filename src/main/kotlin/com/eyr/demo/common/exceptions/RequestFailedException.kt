package com.eyr.demo.common.exceptions

import com.eyr.demo.common.constants.ReturnCode


class RequestFailedException(
    val code: ReturnCode,
    val msg: String? = code.msg,
) : RuntimeException(msg)