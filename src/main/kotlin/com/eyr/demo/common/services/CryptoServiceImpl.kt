package com.eyr.demo.common.services

import com.eyr.demo.common.annotations.HazelCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
class CryptoServiceImpl : CryptoService {
    private lateinit var backendRSAKeyPair: Pair<ByteArray, ByteArray>

    private lateinit var frontendPublicKeyByte: ByteArray

    @Value("\${app.cryptography.rsa.algorithms}")
    private val rsaAlgorithms: String = ""

    @Value("\${app.cryptography.aes.algorithms}")
    private val aesAlgorithms: String = ""

    override fun setFrontendPublicKeyByte(key: ByteArray) = run {
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

    @HazelCache(
        name = "crypto",
        key = "rsa-key",
    )
    override fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray> = run {
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

    override fun doRSAEncryption(data: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance(rsaAlgorithms)
        cipher.init(Cipher.ENCRYPT_MODE, getRSAPublicKey())
        cipher.doFinal(data)
    }

    override fun doRSADecryption(data: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance(rsaAlgorithms)
        cipher.init(Cipher.DECRYPT_MODE, getRSAPrivateKey())
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