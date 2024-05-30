package com.eyr.demo.common.services

import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import javax.crypto.Cipher

@Service
class CryptoService {

    private lateinit var keyPair: Pair<ByteArray, ByteArray>

    private fun getRSAPrivateKey(): PrivateKey = run {
        val privateKey = keyPair.second
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKeySpec = PKCS8EncodedKeySpec(privateKey)
        keyFactory.generatePrivate(privateKeySpec)
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun genRSAKeyPair(seed: String): Pair<ByteArray, ByteArray> = run {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")

        val parameterSpec = RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        secureRandom.setSeed(seed.toByteArray())

        keyPairGenerator.initialize(parameterSpec, secureRandom)

        val keyPairGenerated = keyPairGenerator.generateKeyPair()
        val publicKeyString = keyPairGenerated.public.encoded.map { it.toUByte() }.toUByteArray().toByteArray()
        val privateKeyString = keyPairGenerated.private.encoded.map { it.toUByte() }.toUByteArray().toByteArray()

        keyPair = Pair(publicKeyString, privateKeyString)

        keyPair
    }

    fun doRSADecryption(encrypted: ByteArray): ByteArray = run {
        val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, getRSAPrivateKey())
        cipher.doFinal(encrypted)
    }
}