package com.eyr.demo.common.services.crypto


interface CryptoService {
    var frontendPublicKeyByte: ByteArray
    fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray>
    fun doRSAEncryption(data: ByteArray): ByteArray
    fun doRSADecryption(data: ByteArray): ByteArray
    fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray
    fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray
}