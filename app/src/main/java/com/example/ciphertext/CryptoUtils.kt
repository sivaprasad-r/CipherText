package com.example.ciphertext

import org.libsodium.jni.Sodium
import org.libsodium.jni.crypto.Random
import org.libsodium.jni.keys.KeyPair
import org.libsodium.jni.SodiumConstants
import java.io.File


object CryptoUtils {

    fun generateKeyPair(): String {
        val seed = Random().randomBytes(SodiumConstants.SECRETKEY_BYTES)
        val encryptionKeyPair = KeyPair(seed)

        return encryptionKeyPair.publicKey.toString()

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
