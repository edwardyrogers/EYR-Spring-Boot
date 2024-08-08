package com.eyr.demo.common.services

import org.springframework.stereotype.Service
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class CryptoService {

    private lateinit var backendRSAKeyPair: Pair<ByteArray, ByteArray>

    private lateinit var frontendPublicKeyByte: ByteArray

    @OptIn(ExperimentalUnsignedTypes::class)
    fun setFrontendPublicKeyByte(key: ByteArray) = run {
        frontendPublicKeyByte = key
    }

    private fun getRSAPublicKey(): PublicKey = run {
        val publicKey = frontendPublicKeyByte
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(publicKey)
        keyFactory.generatePublic(publicKeySpec)
    }

    private fun getRSAPrivateKey(): PrivateKey = run {
        val privateKey = backendRSAKeyPair.second
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKeySpec = PKCS8EncodedKeySpec(privateKey)
        keyFactory.generatePrivate(privateKeySpec)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray> = run {
        if (::backendRSAKeyPair.isInitialized) return backendRSAKeyPair

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val parameterSpec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")

        secureRandom.setSeed(ByteArray(32) { SecureRandom().nextInt(256).toByte() })

        keyPairGenerator.initialize(parameterSpec, secureRandom)

        val keyPairGenerated = keyPairGenerator.generateKeyPair()
        val publicKeyString = keyPairGenerated.public.encoded
        val privateKeyString = keyPairGenerated.private.encoded

        backendRSAKeyPair = Pair(publicKeyString, privateKeyString)
        backendRSAKeyPair
    }

    fun doRSAEncryption(data: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getRSAPublicKey())
        cipher.doFinal(data)
    }

    fun doRSADecryption(data: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, getRSAPrivateKey())
        cipher.doFinal(data)
    }

    fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(key))
        cipher.doFinal(data)
    }

    fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(key))
        cipher.doFinal(data)
    }
}