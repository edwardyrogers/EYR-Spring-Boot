package com.eyr.demo.common.hidden_endpoint

import com.eyr.demo.common.constants.ReturnCode
import com.eyr.demo.common.exceptions.ServiceException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Aspect
@Component
class HiddenEndpointAspect(
    private val _environment: Environment
) {
    @Before("@annotation(hiddenEndpoint)")
    fun beforeMethodExecution(joinPoint: JoinPoint, hiddenEndpoint: HiddenEndpoint) = run {
        // You can add more custom logic here based on the NotExposure annotation
        if (hiddenEndpoint.isForceShown) return@run

        if (_environment.activeProfiles.contains("prod")) {
            throw ServiceException(
                ReturnCode.ENDPOINT_NOT_FOUND
            )
        }
    }
}
