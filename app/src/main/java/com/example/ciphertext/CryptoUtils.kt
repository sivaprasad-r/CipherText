package com.example.ciphertext

import org.libsodium.jni.Sodium
import org.libsodium.jni.crypto.Random
import org.libsodium.jni.keys.KeyPair
import org.libsodium.jni.SodiumConstants


object CryptoUtils {

    private const val ANDROID_KEYSTORE_PROVIDER = "AndroidKeyStore"
    private const val KEY_ALIAS = "my_key_alias"

    data class KeyPairData(val publicKey: String, val privateKey: String)

    fun generateKeyPair(): KeyPairData {
        val seed = Random().randomBytes(SodiumConstants.SECRETKEY_BYTES)
        val encryptionKeyPair = KeyPair(seed)
        val privateKey = encryptionKeyPair.privateKey.toString()
        val publicKey = encryptionKeyPair.publicKey.toString()
        return KeyPairData(publicKey, privateKey)
    }

    fun encryptData(publicKey: ByteArray, data: ByteArray): ByteArray {
        val nonce = Random().randomBytes(SodiumConstants.NONCE_BYTES)
        val encryptedData = ByteArray(data.size + Sodium.crypto_box_macbytes())

        Sodium.crypto_box_seal(encryptedData, data, data.size, publicKey)

        return encryptedData
    }

    fun decryptData(privateKey: ByteArray, encryptedData: ByteArray): ByteArray {
        val decryptedData = ByteArray(encryptedData.size - Sodium.crypto_box_macbytes())
        val nonce = ByteArray(Sodium.crypto_box_noncebytes())

        Sodium.crypto_box_seal_open(decryptedData, encryptedData, encryptedData.size.toInt(), nonce, privateKey)

        return decryptedData
    }
}
