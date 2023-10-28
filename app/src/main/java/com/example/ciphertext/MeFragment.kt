package com.example.ciphertext

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*
import java.io.File


class MeFragment : Fragment() {

    private lateinit var qrCodeImageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_me, container, false)

        qrCodeImageView = view.findViewById(R.id.qrCodeImageView)

        val btnCreateKeypair: Button = view.findViewById(R.id.btnCreateKeypair)

        btnCreateKeypair.setOnClickListener {
            val keyPairData = CryptoUtils.generateKeyPair()
            val publicKey = keyPairData.publicKeyHex
            val privateKey = keyPairData.privateKeyHex
            val nonce = keyPairData.nonceHex

            // Save public key to publicKey.txt
            val publicKeyFile = File(requireContext().filesDir, "publicKey.txt")
            publicKeyFile.writeText(publicKey)

            // Save private key to privateKey.txt
            val privateKeyFile = File(requireContext().filesDir, "privateKey.txt")
            privateKeyFile.writeText(privateKey)
            updateQRCode()

            val nonceFile = File(requireContext().filesDir, "nonce.txt")
            nonceFile.writeText(nonce)
        }

        updateQRCode()

        return view
    }

    private fun updateQRCode() {
        val fileName = "publicKey.txt"
        val file = File(requireContext().filesDir, fileName)

        if (file.exists()) {
            val qrCodeContent = file.readText(Charsets.UTF_8)
            val qrCodeBitmap = generateQRCode(qrCodeContent)
            qrCodeImageView.setImageBitmap(qrCodeBitmap)
        }
    }


    private fun generateQRCode(content: String): Bitmap? {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        try {
            val bitMatrix: BitMatrix =
                MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 1000, 1000, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix[x, y]) {
                        // Black pixel
                        0xFF000000.toInt()
                    } else {
                        // White pixel
                        0xFFFFFFFF.toInt()
                    }
                }
            }
            val qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return qrCodeBitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }
}
