package com.policyboss.demoandroidapp.HiltDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityHiltDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HiltDemoActivity : AppCompatActivity() {

    lateinit var binding: ActivityHiltDemoBinding

    // Field Injection
    @Inject
    lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHiltDemoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Hilt Demo"
        }

//        val engine =  Engine()
//        val car = Car(engine)
        car.startCar()
    }
}


class Car (val engine: Engine){

    fun startCar(){
        engine.getStartService()
        Log.d(Constant.TAG,"Car gat Started , Start car")

    }
}

// Constructor Injection
// we
class Engine @Inject constructor(val piston: Piston){

    fun  getStartService(){

      piston.pistonStarted()

        Log.d(Constant.TAG,"Enginee gat Started")
    }
}

//class Engine {
//
//    fun  getStartService(){
//
//        Log.d(Constant.TAG,"Enginee gat Started")
//    }
//}
class Piston @Inject constructor(){

    fun pistonStarted(){
        Log.d(Constant.TAG,"Piston gat Started")

    }
}