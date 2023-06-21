package com.example.ciphertext

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

class MeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_me, container, false)

        // Generate QR code and set it to ImageView
        val qrCodeImageView: ImageView = view.findViewById(R.id.qrCodeImageView)
        val qrCodeContent = "Hello, QR Code!"
        val qrCodeBitmap = generateQRCode(qrCodeContent)
        qrCodeImageView.setImageBitmap(qrCodeBitmap)

        return view
    }

    private fun generateQRCode(content: String): Bitmap? {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        try {
            val bitMatrix: BitMatrix =
                MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 500, 500, hints)
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
