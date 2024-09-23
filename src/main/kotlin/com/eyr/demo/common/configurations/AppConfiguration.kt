package com.eyr.demo.common.configurations

import com.eyr.demo.common.services.crypto.CryptoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration(
    private var cryptoService: CryptoService
) {
    @Bean
    fun genRSAKeyPair() = run { cryptoService.genOrGetRSAKeyPair() }
}