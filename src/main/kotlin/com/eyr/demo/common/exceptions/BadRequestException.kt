package com.eyr.demo.common.exceptions

import com.eyr.demo.common.constants.AppReturnCode


class BadRequestException(
    val code: AppReturnCode,
    override val message: String?,
) : RuntimeException()