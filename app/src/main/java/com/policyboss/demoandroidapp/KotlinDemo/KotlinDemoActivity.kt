package com.policyboss.demoandroidapp.KotlinDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import com.policyboss.demoandroidapp.Constant
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
        binding.btnMap.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
    }



    override fun onClick(view: View?) {

        when(view?.id) {

           binding.btnCompanionDemo.id -> {

               calledCompanion()
           }

            binding.btnMap.id -> {

                mapDemo()
            }

            binding.btnClose.id -> {

                this.finish()
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

    fun mapDemo(){


        val emp: List<Employee> = listOf(
            Employee(1, "John"),
            Employee(2, "Alice"),
            Employee(3, "Bob")
        )

        val empDetail: List<EmployeeDetail> = emp.map { employee ->
            // Create EmployeeDetail objects using the data from the emp list
            EmployeeDetail(
                id = employee.id,
                name = employee.name,
                detail = "Default Detail", // You can replace this with the actual detail value
                mob = "Default Mobile"     // You can replace this with the actual mobile value
            )
        }

        Log.d(Constant.TAG,empDetail.toString())
    }

}

data class Employee(val id: Int, val name: String)
data class EmployeeDetail(val id: Int, val name: String, val detail: String, val mob: String)


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

