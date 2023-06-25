package com.example.ciphertext

import org.libsodium.jni.Sodium
import org.libsodium.jni.crypto.Random
import org.libsodium.jni.keys.KeyPair
import org.libsodium.jni.SodiumConstants
import java.security.KeyStore
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import org.libsodium.jni.keys.PrivateKey as SodiumPrivateKey
import java.security.KeyFactory




object CryptoUtils {

    fun generateKeyPair(): String {
        val seed = Random().randomBytes(SodiumConstants.SECRETKEY_BYTES)
        val encryptionKeyPair = KeyPair(seed)
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val alias = "my_private_key_alias"
        val privateKey = convertToJavaPrivateKey(encryptionKeyPair.privateKey)

        // Store the private key entry in the KeyStore
        keyStore.setKeyEntry(alias, privateKey, null, null)
        //Return public key
        return encryptionKeyPair.publicKey.toString()

    }
    //Inorder to store the key inside keyStore, need convert the Sodium private key to Java's PrivateKey
    private fun convertToJavaPrivateKey(sodiumPrivateKey: SodiumPrivateKey): PrivateKey {
        val privateKeyBytes = sodiumPrivateKey.toBytes()
        val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }
    fun retrievePrivateKey(): org.libsodium.jni.keys.PrivateKey? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        // Alias of the private key entry you stored previously
        val alias = "my_private_key_alias"

        // Retrieve the private key from the KeyStore
        val storedPrivateKey = keyStore.getKey(alias, null) as PrivateKey
        return convertToSodiumPrivateKey(storedPrivateKey)
    }
    private fun convertToSodiumPrivateKey(privateKey: PrivateKey): SodiumPrivateKey? {
        val keyFactory = KeyFactory.getInstance(privateKey.algorithm)
        val keySpec = keyFactory.getKeySpec(privateKey, PKCS8EncodedKeySpec::class.java)
        val encodedPrivateKey = keySpec.encoded

        try {
            return SodiumPrivateKey(encodedPrivateKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
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
