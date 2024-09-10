package com.eyr.demo.common.services

import java.security.PrivateKey
import java.security.PublicKey


interface CryptoService {
    @OptIn(ExperimentalUnsignedTypes::class)
    fun setFrontendPublicKeyByte(key: ByteArray)
    private fun getRSAPublicKey(): PublicKey = throw NotImplementedError()
    private fun getRSAPrivateKey(): PrivateKey = throw NotImplementedError()
    fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray>
    fun doRSAEncryption(data: ByteArray): ByteArray
    fun doRSADecryption(data: ByteArray): ByteArray
    fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray
    fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray
}