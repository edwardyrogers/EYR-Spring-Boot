package com.eyr.demo.common.annotations

import kotlin.annotation.AnnotationTarget.*
import kotlin.annotation.Retention
import kotlin.annotation.AnnotationRetention.RUNTIME

@Target(FUNCTION) // This annotation can be applied to functions
@Retention(RUNTIME) // The annotation is available at runtime
annotation class ExposureOnPropertyFactor
