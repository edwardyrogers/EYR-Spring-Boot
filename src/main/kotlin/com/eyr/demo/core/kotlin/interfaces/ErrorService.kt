package com.eyr.demo.core.kotlin.interfaces

import kotlin.reflect.KClass

interface ErrorService {
    fun formatErrorCode(errCode: String, where: KClass<*>): String
}