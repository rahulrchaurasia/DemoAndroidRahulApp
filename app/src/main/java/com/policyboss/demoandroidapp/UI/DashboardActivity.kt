package com.policyboss.demoandroidapp.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.policyboss.demoandroidapp.LocationDemo.LocationDemoActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityDashboardBinding
import com.policyboss.demoandroidapp.databinding.ActivityFlowDemoBinding

class DashboardActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var binding : ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "DashBoard"

            setClickListener()
        }


    }

    fun setClickListener() {

        binding.btnLocationDemo.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnLocationDemo.id -> {



                val intent = Intent(this, LocationDemoActivity::class.java).apply {
                    putExtra("message", "Hello, TargetActivity!")
                }

                startActivity(intent)
            }
        }
    }
}