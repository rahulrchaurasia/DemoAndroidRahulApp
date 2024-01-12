package com.policyboss.demoandroidapp.ShareData

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.TextScanner.AutoTextReaderActivity
import com.policyboss.demoandroidapp.databinding.ActivityDashboardBinding
import com.policyboss.demoandroidapp.databinding.ActivityShareMainBinding

class ShareActivityMain :BaseActivity() , View.OnClickListener {

    lateinit var binding : ActivityShareMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_main)

        binding = ActivityShareMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Share Demo"


        }
        setOnClickListner()
    }

    fun setOnClickListner(){

        binding.btnShare1.setOnClickListener(this)
        binding.btnShare2.setOnClickListener(this)
    }


    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnShare1.id -> {

                startActivity(Intent(this, ShareViwAsActivity::class.java))

            }

            binding.btnShare2.id -> {


                startActivity(Intent(this, ShareActivity::class.java))
            }
        }
    }
}