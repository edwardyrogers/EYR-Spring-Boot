package com.eyr.demo.common.configurations

import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig
import com.hazelcast.core.HazelcastInstance
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HazelcastConfiguration {
    @Bean
    fun hazelcastInstance(): HazelcastInstance {
        val clientConfig = ClientConfig().apply {
            clusterName = "eyr-one-piece"
            networkConfig.apply {
//                addAddress("eyr-hazelcast:5701")
                connectionTimeout = 5000
            }
        }
        return HazelcastClient.newHazelcastClient(clientConfig)
    }
}