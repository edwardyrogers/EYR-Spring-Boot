package com.eyr.demo.common.services.crypto

import com.eyr.demo.common.annotations.HazelCache
import com.eyr.demo.common.services.hazelcast.HazelcastService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class CryptoServiceImpl(
    private val hazelcastService: HazelcastService
) : CryptoService {
    @Value("\${app.cryptography.rsa.algorithms}")
    private val rsaAlgorithms: String = ""

    @Value("\${app.cryptography.aes.algorithms}")
    private val aesAlgorithms: String = ""

    override lateinit var frontendPublicKeyByte: ByteArray

    @HazelCache(key = "rsa-key")
    override fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray> = run {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val parameterSpec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")

        secureRandom.setSeed(ByteArray(32) { SecureRandom().nextInt(256).toByte() })

        keyPairGenerator.initialize(parameterSpec, secureRandom)

        val keyPairGenerated = keyPairGenerator.generateKeyPair()
        val publicKeyString = keyPairGenerated.public.encoded
        val privateKeyString = keyPairGenerated.private.encoded

        Pair(publicKeyString, privateKeyString)
    }

    override fun doRSAEncryption(data: ByteArray): ByteArray = run {
        val publicKey = frontendPublicKeyByte
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(publicKey)
        val rsaPublicKey = keyFactory.generatePublic(publicKeySpec)
        val cipher: Cipher = Cipher.getInstance(rsaAlgorithms)
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)
        cipher.doFinal(data)
    }

    override fun doRSADecryption(data: ByteArray): ByteArray = run {
        val keyPair = hazelcastService.getOrCache(
            map = "CryptoServiceImpl.GenOrGetRSAKeyPair",
            key = "rsa-key"
        ) {
            genOrGetRSAKeyPair()
        } as Pair<*, *>

        val privateKey = keyPair.second as ByteArray
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKeySpec = PKCS8EncodedKeySpec(privateKey)
        val rsaPrivateKey = keyFactory.generatePrivate(privateKeySpec)
        val cipher: Cipher = Cipher.getInstance(rsaAlgorithms)
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey)
        cipher.doFinal(data)
    }

    override fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance(aesAlgorithms)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(key.copyOfRange(16, key.size)))
        cipher.doFinal(data)
    }

    override fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance(aesAlgorithms)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(key.copyOfRange(16, key.size)))
        cipher.doFinal(data)
    }
}