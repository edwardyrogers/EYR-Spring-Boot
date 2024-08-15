package com.eyr.demo.common.annotations

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION) // This annotation can be applied to functions
@Retention(RUNTIME) // The annotation is available at runtime
annotation class ExposureOnPropertyFactor
