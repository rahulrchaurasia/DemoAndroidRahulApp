package com.policyboss.demoandroidapp.ShareData

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityHomeBinding
import com.policyboss.demoandroidapp.databinding.ActivityShareBinding

class ShareActivity : AppCompatActivity() {


    lateinit var binding: ActivityShareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}