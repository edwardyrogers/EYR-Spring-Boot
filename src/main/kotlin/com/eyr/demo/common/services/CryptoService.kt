package com.eyr.demo.common.services

import org.springframework.stereotype.Service
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.random.Random

@Service
class CryptoService {

    private lateinit var backendKeyPair: Pair<ByteArray, ByteArray>

    private lateinit var frontendPublicKeyByte: ByteArray

    @OptIn(ExperimentalUnsignedTypes::class)
    fun setFrontendPublicKeyByte(key: ByteArray) = run {
        frontendPublicKeyByte = key.map { it.toUByte() }.toUByteArray().toByteArray()
    }

    private fun getRSAPublicKey(): PublicKey = run {
        val publicKey = frontendPublicKeyByte
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(publicKey)
        keyFactory.generatePublic(publicKeySpec)
    }

    private fun getRSAPrivateKey(): PrivateKey = run {
        val privateKey = backendKeyPair.second
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKeySpec = PKCS8EncodedKeySpec(privateKey)
        keyFactory.generatePrivate(privateKeySpec)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun genRSAKeyPair(): Pair<ByteArray, ByteArray> = run {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")

        val parameterSpec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        secureRandom.setSeed(ByteArray(32) { Random.nextInt(256).toByte() })

        keyPairGenerator.initialize(parameterSpec, secureRandom)

        val keyPairGenerated = keyPairGenerator.generateKeyPair()
        val publicKeyString = keyPairGenerated.public.encoded.map { it.toUByte() }.toUByteArray().toByteArray()
        val privateKeyString = keyPairGenerated.private.encoded.map { it.toUByte() }.toUByteArray().toByteArray()

        backendKeyPair = Pair(publicKeyString, privateKeyString)

        backendKeyPair
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
}