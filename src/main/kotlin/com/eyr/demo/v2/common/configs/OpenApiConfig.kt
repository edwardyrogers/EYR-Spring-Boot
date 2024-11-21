import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPIV1(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("v1").pathsToMatch("/api/v1/**")
            .packagesToScan("cc.worldline.customermanagement").addOpenApiCustomiser { openApi ->
                openApi.info =
                    Info().title("Device and Session Management API").version("1.0").description("API for version 1")
            }.build()
    }

    @Bean
    fun customOpenAPI(): GroupedOpenApi {
        return GroupedOpenApi.builder().group("v2").pathsToMatch("/api/v2/**")
            .packagesToScan("cc.worldline.customermanagement.v2").addOpenApiCustomiser { openApi ->
                openApi.info =
                    Info().title("Device and Session Management API").version("2.0").description("API for version 2")
            }.build()
    }
}