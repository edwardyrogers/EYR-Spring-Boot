package cc.worldline.common.data.crypto

import cc.worldline.common.constants.ReturnCode
import cc.worldline.common.exceptions.ServiceException
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
class CryptoServiceImpl(
    private val hazelcastInstance: HazelcastInstance?
) : CryptoService {

    @Value("\${cryptography.enabled}")
    private var isCryptoEnabled = false

    @Value("\${cryptography.rsa.algorithms}")
    lateinit var rsaAlgo: String

    @Value("\${cryptography.aes.algorithms}")
    lateinit var aesAlgo: String

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
            val keyPairMap = hazelcastInstance?.getMap<Any, Any>("GenOrGetRSAKeyPair")
                ?: throw ServiceException(
                    ReturnCode.INVALID, "HazelCast map name"
                )

            val keyPair = keyPairMap["rsa-key"].let {
                @Suppress("UNCHECKED_CAST")
                it as? Pair<ByteArray, ByteArray>
            } ?: throw ServiceException(
                ReturnCode.NOT_FOUND, "req by keyPairMap[\"rsa-key\"]"
            )

            val keyFactory = KeyFactory.getInstance("RSA")
            val privateKey = keyPair.second
            val privateKeySpec = PKCS8EncodedKeySpec(privateKey)
            keyFactory.generatePrivate(privateKeySpec)
        }

    override fun setRSAFrontendPubKeyByte(key: ByteArray) = run {
        frontendRSAPubKeyByte = key
    }

    @Cacheable(
        cacheNames = ["GenOrGetRSAKeyPair"],
        key = "'rsa-key'"
    )
    override fun genOrGetRSAKeyPair(): Pair<ByteArray, ByteArray> = run {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val parameterSpec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")

        secureRandom.setSeed(
            ByteArray(32) { SecureRandom().nextInt(256).toByte() }
        )

        keyPairGenerator.initialize(parameterSpec, secureRandom)

        val keyPairGenerated = keyPairGenerator.generateKeyPair()
        val publicKeyString = keyPairGenerated.public.encoded
        val privateKeyString = keyPairGenerated.private.encoded

        Pair(publicKeyString, privateKeyString)
    }

    override fun doRSAEncryption(data: ByteArray): ByteArray = run {
        val cipher = Cipher.getInstance(rsaAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey)
        cipher.doFinal(data)
    }

    override fun doRSADecryption(data: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance(rsaAlgo)
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey)
        cipher.doFinal(data)
    }

    override fun doAESEncryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance(aesAlgo)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(key.copyOfRange(16, key.size)))
        cipher.doFinal(data)
    }

    override fun doAESDecryption(key: ByteArray, data: ByteArray): ByteArray = run {
        val secretKey = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance(aesAlgo)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(key.copyOfRange(16, key.size)))
        cipher.doFinal(data)
    }

    override fun encrypt(data: ByteArray): CryptoData = run {
        if (isCryptoEnabled) {
            LOGGER.info("--> [Do Encryption]")
            val aesKey = ByteArray(16)
            val encryptedData = doAESEncryption(aesKey, data)
            val encryptedKey = doRSAEncryption(aesKey)
            val res = encryptedKey.plus(encryptedData)
            CryptoData(Base64.getEncoder().encodeToString(res))
        } else {
            CryptoData(String(data))
        }
    }

    override fun decrypt(data: String): ByteArray = run {
        if (isCryptoEnabled) {
            LOGGER.info("<-- [Do Decryption]")
            val encryptedBytes = Base64.getDecoder().decode(data)
            val encryptedKey = encryptedBytes.sliceArray(0 until 256)
            val encryptedData = encryptedBytes.sliceArray(256 until encryptedBytes.size)
            val decryptedKey = doRSADecryption(encryptedKey)
            doAESDecryption(decryptedKey, encryptedData)
        } else {
            data.toByteArray()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CryptoService::class.java)
    }
}