package com.eyr.demo.core.interfaces

import kotlin.reflect.KClass

interface ErrorService {
    fun formatErrorCode(errCode: String, where: KClass<*>): String
}