package com.eyr.demo.core.configs

import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.eyr.demo.core.models.BizLogicModel
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hazelcast.config.SerializerConfig
import com.hazelcast.config.YamlConfigBuilder
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.nio.ObjectDataInput
import com.hazelcast.nio.ObjectDataOutput
import com.hazelcast.nio.serialization.StreamSerializer
import com.hazelcast.spring.cache.HazelcastCacheManager
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable


@Configuration
@EnableCaching
@ConditionalOnProperty(
    name = ["backend-core.caching.enabled"],
    havingValue = "true",
    matchIfMissing = false
)
open class CachingConfig {
    open class BizLogicCachingSerialiser : StreamSerializer<BizLogicModel.RES> {

        private val objectMapper = jacksonObjectMapper().apply {
            registerModule(
                SimpleModule().addDeserializer(Page::class.java, PageDeserializer<Any>())
            )
        }

        // Serialize the object and its type information, including nested types
        override fun write(out: ObjectDataOutput, obj: BizLogicModel.RES) = run {
            // Convert object to JSON string
            val jsonString = objectMapper.writeValueAsString(obj)

            // Get the class name of the object (to preserve type information)
            val (resType, dataType) = when (obj) {
                is BizLogicModel.RES.Single<*> -> {
                    Pair(
                        BizLogicModel.RES.Single::class.java.name,
                        obj.data!!::class.java.name
                    )
                }

                is BizLogicModel.RES.Paginated<*> -> {
                    Pair(
                        BizLogicModel.RES.Paginated::class.java.name,
                        obj.data::class.java.name
                    )
                }

                else -> throw ServiceException(
                    ReturnCode.INVALID, "${obj.javaClass.name} is not subclass of BizLogicModel.RES"
                )
            }

            // Write both the type and the JSON string to the output
            out.writeString(obj.javaClass.name)
            out.writeString(resType)
            out.writeString(dataType)  // Write the class name first
            out.writeString(jsonString)  // Write the JSON string second
        }

        // Read the type information and deserialize the object and its nested types
        override fun read(input: ObjectDataInput): BizLogicModel.RES = run {
            // Read the type information (class name) and the JSON string from the input
            val bizType = input.readString()
            val resType = input.readString()
            val dataType = input.readString()
            val jsonString = input.readString()

            // Dynamically load the class based on the type name
            val resClazz = Class.forName(resType)
            val dataClazz = Class.forName(dataType)
            val clazz = objectMapper.typeFactory.constructParametricType(
                resClazz,
                dataClazz
            )

            LOGGER.info("--- Read ${bizType?.split(".")?.last()} of ${dataType?.split(".")?.last()} on Caching!")

            return@run objectMapper.readValue(jsonString, clazz)
        }

        override fun getTypeId(): Int {
            // Return a unique type ID for this serializer
            return 1
        }

        override fun destroy() {
            // Cleanup if needed
        }
    }

    open class PageDeserializer<T> : StdDeserializer<Page<T>>(Page::class.java) {

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Page<T> = run {
            // Get the type of T from the context
            val node: JsonNode = p.codec.readTree(p)

            // Extract content (List<T>) from the JSON
            val contentNode = node.get("content")
            val content: List<T> = p.codec.readValue(
                contentNode.traverse(),
                object : TypeReference<List<T>>() {}
            )

            // Extract total elements from the JSON
            val totalElements = node.get("totalElements").asLong()

            // Extract pageable information (e.g., page size, page number) from the JSON
            val pageableNode = node.get("pageable")
            val pageNumber = pageableNode?.get("pageNumber")?.asInt() ?: 0
            val pageSize = pageableNode?.get("pageSize")?.asInt() ?: 20
            val pageable: Pageable = PageRequest.of(pageNumber, pageSize)

            // Return a new PageImpl instance (or other Page implementation)
            return@run PageImpl(content, pageable, totalElements)
        }
    }

    @Bean
    open fun hazelcastInstance(): HazelcastInstance = run {
        val config = YamlConfigBuilder(
            this::class.java.classLoader.getResourceAsStream("hazelcast.yaml")
        ).build()

        // Add custom serializer
        val serializationConfig = config.serializationConfig

        serializationConfig.addSerializerConfig(
            SerializerConfig().apply {
                typeClass = BizLogicModel.RES::class.java
                implementation = BizLogicCachingSerialiser()
            }
        )

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

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CachingConfig::class.java)
    }
}