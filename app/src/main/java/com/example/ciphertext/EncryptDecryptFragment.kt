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


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EncryptDecryptFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var etTextArea: EditText
    private lateinit var btnEncrypt: Button
    private lateinit var btnDecrypt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        val fileName = "publicKey.txt"
        val file = File(requireContext().filesDir, fileName)
        val publicKey = file.readText(Charsets.UTF_8).toByteArray()
        val encryptedText = CryptoUtils.encryptData(publicKey, inputText)
        return encryptedText.toString()
    }

    private fun decryptText(text: String): String {
        val cipherText = text.toByteArray()
        val storedPrivateKey = CryptoUtils.retrievePrivateKey().toString()
        val privateKey = storedPrivateKey.toByteArray()
        val decryptedText = CryptoUtils.decryptData(privateKey, cipherText)
        return decryptedText.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EncryptDecryptFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
