package com.eyr.demo.common.services.hazelcast

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import org.springframework.stereotype.Service

@Service
class HazelcastServiceImpl(
    private val hazelcast: HazelcastInstance = Hazelcast.getHazelcastInstanceByName(
        "hsbc-mbk-backend-hazelcast"
    )
) : HazelcastService {
    override fun get(map: String, key: String): Any? = run {
        val cache: IMap<String, Any> = hazelcast.getMap(map)
        cache[key]
    }

    override fun put(map: String, key: String, value: Any): Any? = run {
        val cache: IMap<String, Any> = hazelcast.getMap(map)
        cache.put(key, value)
    }

    override fun getOrCache(map: String, key: String, valueProvider: () -> Any): Any = run {
        get(map, key) ?: run {
            val response = valueProvider()
            put(map, key, response)
            response
        }
    }
}