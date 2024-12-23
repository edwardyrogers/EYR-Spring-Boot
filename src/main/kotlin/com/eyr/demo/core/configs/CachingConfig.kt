package cc.worldline.common.configs

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.spring.cache.HazelcastCacheManager
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableCaching
open class CachingConfig {

    /*
     * Example
     * @Cacheable(
     *    cacheNames = ["cache area name"],
     *    key = "uni-key"
     * )
     */
    @Bean
    open fun cacheManager(
        hazelcastInstance: HazelcastInstance?
    ): CacheManager = run {
        HazelcastCacheManager(hazelcastInstance)
    }
}