package com.policyboss.demoandroidapp.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.databinding.ActivityAsDialogBinding

class customActivityAsDialog : AppCompatActivity() {

    lateinit var binding : ActivityAsDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAsDialogBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.imgClose.setOnClickListener{

            this.finish()
        }
    }
}