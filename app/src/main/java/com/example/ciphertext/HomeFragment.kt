package com.example.ciphertext

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.integration.android.IntentIntegrator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Intent



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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // QR Code scan successful, handle the scanned data
                val scannedData = result.contents
                // Process the scanned data as needed
                // ...
            } else {
                // QR Code scan cancelled or failed
                // Handle the cancellation or failure scenario
                // ...
            }
        }
    }
}
