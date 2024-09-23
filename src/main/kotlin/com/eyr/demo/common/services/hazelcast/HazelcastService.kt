package com.eyr.demo.common.services.hazelcast

interface HazelcastService {
    fun get(map: String, key: String): Any?
    fun put(map: String, key: String, value: Any): Any?
    fun getOrCache(map: String, key: String, valueProvider: () -> Any): Any
}