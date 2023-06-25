package com.example.ciphertext

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.integration.android.IntentIntegrator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent
import android.content.Context
import java.io.File
import android.widget.TextView




class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the Floating Action Button
        val fab: FloatingActionButton = view.findViewById(R.id.fab)

        // Set a click listener for the Floating Action Button
        fab.setOnClickListener {
            // Initialize the IntentIntegrator
            val integrator = IntentIntegrator.forSupportFragment(this)

            // Set desired properties for the QR Code scanner
            integrator.setPrompt("Scan a QR Code")
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        return view
    }

    // Handle the result of the QR Code scanning
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // QR Code scan successful, handle the scanned data
                val scannedData = result.contents
                // Process the scanned data as needed
                val fileName = "scanned_data.txt"
                val fileContents = scannedData.toByteArray()
                val file = File(requireContext().filesDir, fileName)
                file.writeBytes(fileContents)
                // ...

            } else {
                // QR Code scan cancelled or failed
                // Handle the cancellation or failure scenario
                // ...
            }
        }
    }
    //Read and display the content of file
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvScannedData: TextView = view.findViewById(R.id.tvScannedData)

        val fileName = "scanned_data.txt"
        val file = File(requireContext().filesDir, fileName)

        if (file.exists()) {
            // Read the contents of scanned_data.txt file
            val fileContents = file.readText(Charsets.UTF_8)

            // Set the contents to the TextView
            tvScannedData.text = fileContents
        } else {
            // File does not exist, handle the scenario accordingly
            // For example, you can display a message or hide the TextView
            tvScannedData.visibility = View.GONE
        }
    }

}
