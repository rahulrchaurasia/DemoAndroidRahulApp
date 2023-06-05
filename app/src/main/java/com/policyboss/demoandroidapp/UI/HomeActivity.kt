package com.policyboss.demoandroidapp.UI

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DesignPattern.DesignPatternDemoActivity
import com.policyboss.demoandroidapp.FlowDemo.FlowDemoActivity
import com.policyboss.demoandroidapp.HiltDemo.HiltDemoActivity
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavigationDemoMainActivity
import com.policyboss.demoandroidapp.UI.TextRecognizer.TextRecognizerActivity
import com.policyboss.demoandroidapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class HomeActivity : AppCompatActivity() ,View.OnClickListener {

    var CAMERA_PERMISSION_REQUEST_CODE = 101
    lateinit var binding: ActivityHomeBinding

    val numbers = flowOf(1, 2, 3, 4, 5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
        setOnClickListener()

        lifecycleScope.launch {

            flowDemo()
        }
    }

    fun setOnClickListener(){

        binding.btnHilt.setOnClickListener(this)
        binding.btnTextRecog.setOnClickListener(this)
        binding.btnNavComp.setOnClickListener(this)
        binding.btnDesignPattern.setOnClickListener(this)
        binding.btnFlow.setOnClickListener(this)
    }

    suspend fun flowDemo(){

//        numbers
//            .onEach { Log.d(Constant.TAG,"Processing number: $it") }
//            .map { it * it }
//            .onEach { Log.d(Constant.TAG,"Result: $it") }
//            .collect({
//
//                Log.d(Constant.TAG,"Collect:  ${it}" )
//            })



        numbers
            .map { it * it }

            .collect({

                Log.d(Constant.TAG,"Collect:  ${it}" )
            })

    }


    override fun onClick(view: View?) {


        when(view?.id){

            binding.btnNavComp.id -> {

                startActivity(Intent(this, NavigationDemoMainActivity::class.java))

            }

            binding.btnHilt.id -> {

                startActivity(Intent(this, HiltDemoActivity::class.java))

            }

            binding.btnFlow.id -> {

                startActivity(Intent(this, FlowDemoActivity::class.java))

            }

            binding.btnDesignPattern.id -> {

                startActivity(Intent(this, DesignPatternDemoActivity::class.java))

            }

            binding.btnTextRecog.id -> {

               startActivity(Intent(this, TextRecognizerActivity::class.java))

            }
        }
    }
}