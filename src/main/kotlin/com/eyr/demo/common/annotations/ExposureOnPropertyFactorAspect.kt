package com.eyr.demo.common.annotations

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class ExposureOnPropertyFactorAspect {

//    @Value("\${endpoint-annotated.exposed}")
//    private val isExposed: Boolean = false

    @Before("@annotation(exposureOnPropertyFactor)")
    fun beforeMethodExecution(joinPoint: JoinPoint, exposureOnPropertyFactor: ExposureOnPropertyFactor) {
        // Logic to execute before the method with @NotExposure annotation
        val methodName = joinPoint.signature.name

        // Example: Print a message before executing methods annotated with @NotExposure
        println("Before method execution with @ExposureOnPropertyFactor annotation: $methodName")

        // You can add more custom logic here based on the NotExposure annotation
//        if (!isExposed) {
//            throw RequestFailedException(
//                code = ReturnCode.ACCESS_DENIED,
//            )
//        }
    }
}
