package com.policyboss.demoandroidapp.HiltDemo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.databinding.ActivityHiltDemoBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HiltDemoActivity : AppCompatActivity() {

    lateinit var binding: ActivityHiltDemoBinding

    // Field Injection
    @Inject
    lateinit var car: Car

    @Inject
    lateinit var engine: Engine

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

        PrefManager.init(this)
        AppModuleTwo.mainTwo.getMainData()


      //  car.startCar()


       // engine.getStartService()


    }

    fun manuallInterfaceCall(obj :IDemoTwo){

        obj.demoTwoData()
    }
}


class Car @Inject constructor(val engine: Engine){

    fun startCar(){
        engine.getStartService()
        Log.d(Constant.TAG,"Car gat Started , Start car")

    }
}

//// Constructor Injection
//
class Engine @Inject constructor(val piston: Piston){

    fun  getStartService(){

      piston.pistonStarted()

        Log.d(Constant.TAG,"Enginee gat Started")
    }
}


class Piston @Inject constructor() {

    fun pistonStarted(){
        Log.d(Constant.TAG,"Piston gat Started")

    }
}

//********** 1> Manual Injection Demo  **********

interface IDemoTwo {

    fun demoTwoData()
}

class DemoTwoImplementation : IDemoTwo{
    override fun demoTwoData() {
       Log.d(Constant.TAG, "Deom tow Implemented...")
    }

}



class MainTwo(private val demoTwo: IDemoTwo) // passing  varibale of type interface
{

    fun getMainData(){

        demoTwo.demoTwoData()

    }
}

object AppModuleTwo{

    var mainTwo =  MainTwo(DemoTwoImplementation() )

}

/////////

object PrefManager {
    private lateinit var pref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // shared pref mode


    fun init(context: Context) {
        pref = context.getSharedPreferences("Your_Pref_Name", Context.MODE_PRIVATE)
        editor = pref.edit()
    }

    fun getString(key: String, defaultValue: String): String {
        return pref.getString(key, defaultValue) ?: defaultValue
    }

    fun putString(key: String, value: String) {
        editor.putString(key, value).apply()
    }


}