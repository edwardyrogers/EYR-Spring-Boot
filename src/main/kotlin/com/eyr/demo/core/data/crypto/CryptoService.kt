package com.eyr.demo.core.data.crypto

interface CryptoService {
    fun setRSAFrontendPubKeyByte(key: ByteArray)
    fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray>
    fun doRSAEncryption(data: ByteArray): ByteArray
    fun doRSADecryption(data: ByteArray): ByteArray
    fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray
    fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray
    fun encrypt(data: ByteArray): CryptoData
    fun decrypt(data: String): ByteArray
}