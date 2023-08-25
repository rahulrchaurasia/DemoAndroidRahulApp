package com.policyboss.demoandroidapp.KotlinDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityKotlinDemoBinding


/*
 companion object is initialized first,
 and then the init block is executed.
 */
class KotlinDemoActivity : AppCompatActivity() , OnClickListener{

    lateinit var binding : ActivityKotlinDemoBinding
     var  TAG = "DEMO"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKotlinDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListener()
    }

    fun setOnClickListener(){

        binding.btnCompanionDemo.setOnClickListener(this)
    }



    override fun onClick(view: View?) {

        when(view?.id) {

           binding.btnCompanionDemo.id -> {

               calledCompanion()
           }

        }
    }


    fun calledCompanion(){
        // accessing static methods (which uses private variable)
        var student1 = StudentData("Abhishek", 18)
        Log.d(TAG, ""+ StudentData.getStudentCount())

        var student2 = StudentData("Karthik", 18)
        Log.d(TAG, ""+StudentData.getStudentCount())

// accessing static parameter
        Log.d(TAG, ""+StudentData.SCHOOL_NAME)
    }

}



/*
In Kotlin, during the initialization process of a class, the companion object is initialized first,
 and then the init block is executed. This means that when an instance of the class is created, the following sequence occurs:

The companion object StudentStats is initialized.
The init block of the class is executed.

 */
class StudentData(var name: String, var age: Int) {
    init {
        noOfStudents += 1             // execute after companion object initialized
    }

    companion object StudentStats {
        private var noOfStudents : Int = 0
        fun getStudentCount():Int = noOfStudents
        val SCHOOL_NAME = "St Stephens"
    }

}

