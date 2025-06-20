package com.eyr.demo.core.data.crypto

import com.eyr.demo.core.constants.ReturnCode
import com.eyr.demo.core.exceptions.ServiceException
import com.hazelcast.core.HazelcastInstance
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Service
open class CryptoServiceImpl(
    private val hazelcastInstance: HazelcastInstance?
) : CryptoService {

    @Value("\${backend-core.crypto.enabled:false}")
    private val isCryptoEnabled = false

    @Value("\${backend-core.crypto.rsa.algorithms:RSA/ECB/PKCS1Padding}")
    private val rsaAlgo: String = "RSA/ECB/PKCS1Padding"

    @Value("\${backend-core.crypto.aes.algorithms:AES/CBC/PKCS5Padding}")
    private val aesAlgo: String = "AES/CBC/PKCS5Padding"

    private lateinit var frontendRSAPubKeyByte: ByteArray

    private val rsaPublicKey: PublicKey
        get() = run {
            val publicKey = frontendRSAPubKeyByte
            val keyFactory = KeyFactory.getInstance("RSA")
            val publicKeySpec = X509EncodedKeySpec(publicKey)
            keyFactory.generatePublic(publicKeySpec)
        }

    private val rsaPrivateKey: PrivateKey
        get() = run {
            if (hazelcastInstance == null) {
                throw ServiceException(
                    ReturnCode.INVALID,
                    ": Please enable caching to save RSA key pair to avoid different key used on different VM"
                )
            }

            val keyPairMap = hazelcastInstance.getMap<Any, Any>(
                "for-a-day-cache"
            )

            val keyPair: Pair<ByteArray, ByteArray> = keyPairMap["rsa-key"].let {
                @Suppress("UNCHECKED_CAST")
                it as? Pair<ByteArray, ByteArray>
            } ?: run {
                val newKey = genOrGetRSAKeyPair()
                keyPairMap.put("rsa-key", newKey)
                newKey
            }

            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKey = keyPair.second
            val privateKeySpec = PKCS8EncodedKeySpec(privateKey)

            return@run keyFactory.generatePrivate(privateKeySpec)
        }

    override fun setRSAFrontendPubKeyByte(key: ByteArray) = run {
        frontendRSAPubKeyByte = key
    }

    @Cacheable(
        cacheNames = ["for-a-day-cache"],
        key = "'rsa-key'"
    )
    override fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray> = run {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val parameterSpec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        val secureRandom = SecureRandom()

        keyPairGenerator.initialize(parameterSpec, secureRandom)

        val keyPairGenerated = keyPairGenerator.generateKeyPair()
        val publicKeyString = keyPairGenerated.public.encoded
        val privateKeyString = keyPairGenerated.private.encoded

        return@run Pair(publicKeyString, privateKeyString)
    }

    override fun doRSAEncryption(data: ByteArray): ByteArray = run {
        val cipher = Cipher.getInstance(rsaAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)
        return@run cipher.doFinal(data)
    }

    override fun doRSADecryption(data: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance(rsaAlgo)
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey)
        return@run cipher.doFinal(data)
    }

    override fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val iv = key.copyOfRange(0, 16)
        val actualKey = key.copyOfRange(16, key.size)
        val secretKey = SecretKeySpec(actualKey, "AES")
        val cipher = Cipher.getInstance(aesAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        cipher.doFinal(data)
    }

    override fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val iv = key.copyOfRange(0, 16)
        val actualKey = key.copyOfRange(16, key.size)
        val secretKey = SecretKeySpec(actualKey, "AES")
        val cipher = Cipher.getInstance(aesAlgo)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        cipher.doFinal(data)
    }

    override fun encrypt(data: ByteArray): CryptoData = run {
        if (isCryptoEnabled) {
            LOGGER.info("--> [Do Encryption]")
            val aesKey = ByteArray(16)
            val encryptedData = doAESEncryption(aesKey, data)
            val encryptedKey = doRSAEncryption(aesKey)
            val res = encryptedKey.plus(encryptedData)
            return@run CryptoData(Base64.getEncoder().encodeToString(res))
        } else {
            return@run CryptoData(String(data))
        }
    }

    override fun decrypt(data: String): ByteArray = run {
        if (isCryptoEnabled) {
            LOGGER.info("<-- [Do Decryption]")
            val encryptedBytes = Base64.getDecoder().decode(data)
            val encryptedKey = encryptedBytes.sliceArray(0 until 256)
            val encryptedData = encryptedBytes.sliceArray(256 until encryptedBytes.size)
            val decryptedKey = doRSADecryption(encryptedKey)
            return@run doAESDecryption(decryptedKey, encryptedData)
        } else {
            return@run data.toByteArray()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CryptoService::class.java)
    }
}