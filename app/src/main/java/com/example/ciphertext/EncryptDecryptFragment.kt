package com.example.ciphertext

import com.example.ciphertext.CryptoUtils
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

    private fun encryptText(text: String): String {
        val inputText = text.toByteArray()
        val fileName = "scanned_data.txt"
        val file = File(requireContext().filesDir, fileName)
        val publicKey = file.readText(Charsets.UTF_8).toByteArray()
        val encryptedText = CryptoUtils.encryptData(publicKey, inputText)
        return encryptedText.toString()
    }

    private fun decryptText(text: String): String {
        val cipherText = text.toByteArray()
        val fileName = "privateKey.txt"
        val file = File(requireContext().filesDir, fileName)
        val privateKey = file.readText(Charsets.UTF_8).toByteArray()
        val decryptedText = CryptoUtils.decryptData(privateKey, cipherText)
        return decryptedText.toString()
    }
}
