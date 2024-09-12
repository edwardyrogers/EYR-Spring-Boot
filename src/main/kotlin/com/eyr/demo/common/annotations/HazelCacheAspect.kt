package com.eyr.demo.common.annotations

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Aspect
@Component
class HazelCacheAspect {
    private val hazelcast: HazelcastInstance = Hazelcast.getHazelcastInstanceByName("eyr-hazelcast")

    @Around("@annotation(hazelCache)")
    @Throws(Throwable::class)
    fun aroundAdvice(joinPoint: ProceedingJoinPoint, hazelCache: HazelCache): Any {
        val name = hazelCache.name
        val key = hazelCache.key
        val lifetime = hazelCache.lifetime

        // Get the cache map
        val cache: IMap<String, Any> = hazelcast.getMap(name)

        // Try to get the value from the cache
        val cachedValue = cache[key]
        if (cachedValue != null) {
            return cachedValue
        }

        // Proceed with the original method call
        val response = joinPoint.proceed()

        // Cache the response if necessary
        if (lifetime > 0) {
            cache.put(key, response, lifetime, TimeUnit.SECONDS)
        } else {
            cache.put(key, response)
        }

        return response
    }
}