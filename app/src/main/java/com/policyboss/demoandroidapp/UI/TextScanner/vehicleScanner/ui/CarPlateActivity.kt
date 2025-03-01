package com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.UI.TextScanner.vehicleScanner.old.VehiclePlateReaderOLDActivity
import com.policyboss.demoandroidapp.databinding.ActivityCarPlateBinding

class CarPlateActivity : BaseActivity() , View.OnClickListener{

    lateinit var binding : ActivityCarPlateBinding



    // Activity result launcher for the VehiclePlateReaderActivity
    private val vehiclePlateReaderLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Get the detected text from the result intent
           // val detectedText = result.data?.getStringExtra(Constant.KEY_DETECT_TEXT)

            val detectedTextBytes = result.data?.getByteArrayExtra(Constant.KEY_DETECT_TEXT)
            val detectedText = detectedTextBytes?.toString(Charsets.UTF_8)

            // Update the EditText with the detected text
            detectedText?.let {
                binding.editTextVehicleNumber.setText(it)
                updateClearButtonVisibility()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarPlateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Back button click listener
        binding.backButton.setOnClickListener(this)

        // Clear button click listener
        binding.clearButton.setOnClickListener(this)

        // Search button click listener
        binding.searchButton.setOnClickListener(this)

        // Scan button click listener
        binding.scanButton.setOnClickListener(this)

        // Voice button click listener
        binding.voiceButton.setOnClickListener(this)

        // EditText text change listener
        binding.editTextVehicleNumber.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                updateClearButtonVisibility()
            }
        })
    }

    private fun updateClearButtonVisibility() {
        binding.clearButton.visibility = if (binding.editTextVehicleNumber.text!!.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun performSearch() {
        val vehicleNumber = binding.editTextVehicleNumber.text.toString().trim()
        if (vehicleNumber.isNotEmpty()) {
            // Add your logic to search for the vehicle number
            // This could be starting a new activity or making an API call
            // ...
        }
    }

    private fun launchVehiclePlateReader() {
        val intent = Intent(this, VehiclePlateReaderActivity::class.java)
        vehiclePlateReaderLauncher.launch(intent)
    }

    private fun launchVehiclePlateReaderOLD() {
        val intent = Intent(this, VehiclePlateReaderOLDActivity::class.java)
        vehiclePlateReaderLauncher.launch(intent)
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            binding.backButton.id -> {
                finish()
            }
            binding.clearButton.id -> {
                binding.editTextVehicleNumber.text!!.clear()
                updateClearButtonVisibility()
            }
            binding.searchButton.id -> {
                performSearch()
            }
            binding.scanButton.id -> {

                launchVehiclePlateReader()
            }
            binding.voiceButton.id -> {


            }
        }
    }


}