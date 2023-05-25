package com.policyboss.demoandroidapp.DesignPattern

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DesignPattern.BuilderPattern.Laptop
import com.policyboss.demoandroidapp.DesignPattern.BuilderPattern.Person
import com.policyboss.demoandroidapp.DesignPattern.Faactory.ShapeFactory
import com.policyboss.demoandroidapp.databinding.ActivityDesignPatternDemoBinding

class DesignPatternDemoActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding : ActivityDesignPatternDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityDesignPatternDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Design Pattern Demo"
        }

        setOnClickListener()
    }

    fun setOnClickListener(){

        binding.btnBuilder.setOnClickListener(this)
        binding.btnFactory.setOnClickListener(this)
    }


    fun callBuilderPattern(){




        val person = Person.Builder()
            .setFirstName("Rahul")
            .setLastName("Chaurasia")
            .setAge(32)
            .setGender("Male")
            .build()


        Log.d(Constant.TAG, "Name: ${person.firstName.toString()}  ${person.lastName.toString()}  Age: ${person.age.toString()} Gender: ${person.gender.toString()} ")
    }

    fun callBuilderPatternDemo2(){
        //NOTE: THIS ONE IS MORE CLEAR

       var  laptop =     Laptop.Builder("i7") // processor is compulsory
                     .setRam("8GB")            // this is optional
                    .setBattery("6000MAH")    // this is optional
                       .create()

        Log.d(Constant.TAG, "\n\n ***************Demo 2 **************** ")

        Log.d(Constant.TAG, "processor: ${laptop.processor}  ram: ${laptop.ram}")
//        Log.d(Constant.TAG, Gson().toJson(laptop).toString())
    }



    fun CallFactory(){

        val shapeFactory = ShapeFactory()
        val circle = shapeFactory.getShape("circle")

    }
    override fun onClick(view: View?) {


        when(view?.id){

            binding.btnBuilder.id ->{

               // callBuilderPattern()
                callBuilderPatternDemo2()
            }

            binding.btnFactory.id ->{

                CallFactory()
            }


        }
    }

}