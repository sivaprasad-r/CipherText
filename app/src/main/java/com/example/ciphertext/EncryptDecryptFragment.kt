package com.example.ciphertext

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.io.File


class EncryptDecryptFragment : Fragment() {

    private lateinit var etTextArea: EditText
    private lateinit var btnEncrypt: Button
    private lateinit var btnDecrypt: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_encrypt_decrypt, container, false)

        etTextArea = view.findViewById(R.id.etTextArea)
        btnEncrypt = view.findViewById(R.id.btnEncrypt)
        btnDecrypt = view.findViewById(R.id.btnDecrypt)

        btnEncrypt.setOnClickListener {
            val inputText = etTextArea.text.toString()
            val encryptedText = encryptText(inputText)
            etTextArea.setText(encryptedText)
        }

        btnDecrypt.setOnClickListener {
            val inputText = etTextArea.text.toString()
            val decryptedText = decryptText(inputText)
            etTextArea.setText(decryptedText)
        }

        return view
    }

    private fun readFile(fileName: String): ByteArray {
        val file = File(requireContext().filesDir, fileName)
        return file.readText(Charsets.UTF_8).toByteArray()
    }

    private fun encryptText(text: String): String {
        val inputText = text.toByteArray()
        val destPublicKey = readFile("scanned_data.txt")
        val privateKey = readFile("privateKey.txt")
        val nonce = readFile("nonce.txt")
        val encryptedText = CryptoUtils.encryptData(inputText, nonce, destPublicKey, privateKey)
        return bytesToHex(encryptedText)
    }

    private fun decryptText(text: String): String {
        val cipherText = hexToBytes(text)
        val destPublicKey = readFile("scanned_data.txt")
        val privateKey = readFile("privateKey.txt")
        val nonce = readFile("nonce.txt")
        val decryptedText = CryptoUtils.decryptData(cipherText, nonce, destPublicKey, privateKey)
        return bytesToHex(decryptedText)
    }


    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = hexArray[v ushr 4]
            hexChars[i * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    private fun hexToBytes(hex: String): ByteArray {
        val len = hex.length
        val paddedHex = if (len % 2 == 1) "0$hex" else hex  // Pad with a leading zero if the length is odd
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] =
                ((Character.digit(paddedHex[i], 16) shl 4) + Character.digit(paddedHex[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }


    companion object {
        private val hexArray = "0123456789ABCDEF".toCharArray()

    }
}
