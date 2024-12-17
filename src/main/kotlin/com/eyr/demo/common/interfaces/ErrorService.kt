package com.eyr.demo.common.interfaces

import kotlin.reflect.KClass

interface ErrorService {
    fun formatErrorCode(errCode: String, where: KClass<*>): String
}