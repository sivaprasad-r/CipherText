package com.example.ciphertext

import org.libsodium.jni.Sodium
object CryptoUtils {

    data class KeyPairData(val publicKeyHex: String, val privateKeyHex: String, val nonceHex: String)

    private fun ByteArray.toHexString(): String {
        return joinToString("") { "%02x".format(it) }
    }

    fun generateKeyPair(): KeyPairData {
        val privateKey = ByteArray(Sodium.crypto_box_secretkeybytes())
        val publicKey = ByteArray(Sodium.crypto_box_publickeybytes())

        Sodium.crypto_box_keypair(publicKey, privateKey)

        val nonce = ByteArray(Sodium.crypto_box_noncebytes())

        Sodium.randombytes_buf(nonce, nonce.size)

        val publicKeyHex = publicKey.toHexString()
        val privateKeyHex = privateKey.toHexString()
        val nonceHex = nonce.toHexString()

        return KeyPairData(publicKeyHex, privateKeyHex, nonceHex)
    }

    fun encryptData(data: ByteArray ,nonce: ByteArray ,destPublicKey: ByteArray, privateKey: ByteArray): ByteArray {

        val encryptedData = ByteArray(data.size + Sodium.crypto_box_macbytes())

        Sodium.crypto_box_easy(encryptedData, data, data.size, nonce ,destPublicKey, privateKey)

        return encryptedData
    }

    fun decryptData(encryptedData: ByteArray, nonce: ByteArray, destPublicKey: ByteArray, privateKey: ByteArray): ByteArray {

        val decryptedData = ByteArray(encryptedData.size - Sodium.crypto_box_macbytes())

        Sodium.crypto_box_open_easy(decryptedData, encryptedData, encryptedData.size, nonce, destPublicKey, privateKey)

        return decryptedData
    }
}
