package com.policyboss.demoandroidapp.ConstrainLayoutDemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.Pagging3Demo.Pagging3DemoActivity
import com.policyboss.demoandroidapp.UI.RecyclerViewOperation.RecycleMultiViewTypeActivity
import com.policyboss.demoandroidapp.databinding.ActivityConstrainLayoutMainBinding
import com.policyboss.demoandroidapp.databinding.ActivityRecyclerViewMainBinding

class ConstrainLayoutMainActivity: BaseActivity() , View.OnClickListener{

    lateinit var binding: ActivityConstrainLayoutMainBinding

    private val callback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            this@ConstrainLayoutMainActivity.finish()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConstrainLayoutMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "RecyclerView Demo"


        }

        setClickListener()

        onBackPressedDispatcher.addCallback(this, callback)



    }

    private fun setClickListener() {

        binding.btnDemo1.setOnClickListener(this)

        binding.btnDemo2.setOnClickListener(this)

        binding.btnDemo3.setOnClickListener(this)

        binding.btnDemo4.setOnClickListener(this)

        binding.btnDemo5.setOnClickListener(this)

        binding.btnDemo6.setOnClickListener(this)

        binding.btnDemo7.setOnClickListener(this)


    }


    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnDemo1.id -> {

                startActivity(Intent(this, ConstarinLayoutDemo1Activity::class.java))

            }

            binding.btnDemo2.id -> {

                startActivity(Intent(this, ConstarinLayoutDemo1Activity::class.java))

            }

            binding.btnDemo3.id -> {

            }
            binding.btnDemo4.id -> {

            }

            binding.btnDemo5.id -> {

            }
            binding.btnDemo6.id -> {


            }

            binding.btnDemo7.id -> {

                //  startActivity(Intent(this, CustomTabLayoutActivity::class.java))

            }

        }
    }
}