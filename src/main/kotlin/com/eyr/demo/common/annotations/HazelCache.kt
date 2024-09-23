package com.eyr.demo.common.annotations

/**
 * Annotation to specify caching behavior with Hazelcast.
 *
 * This annotation can be applied to methods to cache their results in a Hazelcast map.
 * The caching parameters allow customization of the cache behavior.
 *
 * @property key The key under which the data will be stored in the Hazelcast map.
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class HazelCache(
    /**
     * The key under which the data will be stored in the Hazelcast map.
     */
    val key: String,
)