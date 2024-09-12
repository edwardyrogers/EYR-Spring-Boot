package com.eyr.demo.common.annotations

/**
 * Annotation to specify caching behavior with Hazelcast.
 *
 * This annotation can be applied to methods to cache their results in a Hazelcast map.
 * The caching parameters allow customization of the cache behavior.
 *
 * @property name The name of the Hazelcast map (cache) where the data will be stored.
 * @property key The key under which the data will be stored in the Hazelcast map.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HazelCache(
    /**
     * The name of the Hazelcast map (cache) where the data will be stored.
     */
    val name: String,

    /**
     * The key under which the data will be stored in the Hazelcast map.
     */
    val key: String,
)