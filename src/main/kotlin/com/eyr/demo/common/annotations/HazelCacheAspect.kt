package com.eyr.demo.common.annotations

import com.eyr.demo.common.services.hazelcast.HazelcastService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class HazelCacheAspect(
    private val hazelcastService: HazelcastService
) {
    @Around("@annotation(hazelCache)")
    @Throws(Throwable::class)
    fun aroundAdvice(joinPoint: ProceedingJoinPoint, hazelCache: HazelCache): Any? = run {
        val clazz = joinPoint.signature.declaringType.name
        val method = joinPoint.signature.name.replaceFirstChar { it.uppercase() }
        hazelcastService.getOrCache("${clazz.substringAfterLast('.')}.$method", hazelCache.key) {
            joinPoint.proceed()
        }
    }
}