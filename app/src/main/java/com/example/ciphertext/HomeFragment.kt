package com.example.ciphertext

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.integration.android.IntentIntegrator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import java.io.File
import android.widget.TextView




class HomeFragment : Fragment() {

    private lateinit var tvScannedData: TextView
    private lateinit var fileName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val fab: FloatingActionButton = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setPrompt("Scan a QR Code")
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        tvScannedData = view.findViewById(R.id.tvScannedData)
        fileName = "scanned_data.txt"

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayScannedData()
    }

    private fun displayScannedData() {
        val file = File(requireContext().filesDir, fileName)

        if (file.exists()) {
            val fileContents = file.readText(Charsets.UTF_8)
            tvScannedData.text = fileContents
        } else {
            tvScannedData.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val scannedData = result.contents
                val fileContents = scannedData.toByteArray()
                val file = File(requireContext().filesDir, fileName)
                file.writeBytes(fileContents)

                // Refresh the fragment by calling displayScannedData()
                displayScannedData()
            } else {
                // Handle the cancellation or failure scenario
                // ...
            }
        }
    }
}
