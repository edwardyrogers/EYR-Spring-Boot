package com.eyr.demo.core.configs

import com.hazelcast.config.YamlConfigBuilder
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.spring.cache.HazelcastCacheManager
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
@ConditionalOnProperty(
    name = ["backend-core.caching.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
open class CachingConfig {

    @Bean
    fun hazelcastInstance(): HazelcastInstance = run {
        val config = YamlConfigBuilder(
            this::class.java.classLoader.getResourceAsStream("hazelcast.yaml")
        ).build()

        // Uncomment to Add custom serializer
//        val serializationConfig = config.serializationConfig
//
//        serializationConfig.addSerializerConfig(
//            SerializerConfig().apply {
//                typeClass = Any::class.java
//                implementation = HazelcastSerialiser()
//            }
//        )

        return@run Hazelcast.newHazelcastInstance(config)
    }

    /*
     * Example
     * @Cacheable(
     *    cacheNames = ["cache area name"],
     *    key = "'uni-key'"
     * )
     */
    @Bean
    open fun cacheManager(
        hazelcastInstance: HazelcastInstance?
    ): CacheManager = run {
        return@run HazelcastCacheManager(hazelcastInstance)
    }
}