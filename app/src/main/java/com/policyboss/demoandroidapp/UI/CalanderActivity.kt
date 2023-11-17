package com.policyboss.demoandroidapp.UI

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCalanderBinding
import com.policyboss.demoandroidapp.databinding.DashboardProdItemModelBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class CalanderActivity : BaseActivity(), OnClickListener {


    lateinit var binding: ActivityCalanderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalanderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentCalendar = Calendar.getInstance()

        val today = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val thisMonth = currentCalendar.get(Calendar.MONTH)
        val thisYear = currentCalendar.get(Calendar.YEAR)

        val previousCalendar = Calendar.getInstance()
       // previousCalendar.set(thisYear, thisMonth - 1, 1)

       // Set the previous calendar to 30 days previous
        previousCalendar.add(Calendar.DAY_OF_MONTH, -31)

        val maxDate =  System.currentTimeMillis()

        val minDate = previousCalendar.timeInMillis


        binding.calendarView.minDate = minDate
        binding.calendarView.maxDate = maxDate


        binding.etFromDate.setOnClickListener(this)
        binding.etToDate.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)


    }


    fun fromDatePicker(){

        // Create a DatePickerDialog object
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                // Get the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Display the selected date in the editText
                binding.etFromDate.setText(SimpleDateFormat("yyyy-MM-dd").format(selectedDate.time))
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        // Set the maximum date of the DatePickerDialog to one month previous

        // Show the date picker dialog
        datePickerDialog.show()
    }

    fun toDatePicker(strFromDate : String){

        // Create a DatePickerDialog object
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, month, dayOfMonth ->
                // Get the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Display the selected date in the editText
                binding.etFromDate.setText(SimpleDateFormat("yyyy-MM-dd").format(selectedDate.time))
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )

        // Set the maximum date of the DatePickerDialog to one month previous

        val previousCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance()

        val toDate = SimpleDateFormat("yyyy-MM-dd").parse(strFromDate)
        calendar.timeInMillis = toDate.time


        // Set the previous calendar to 30 days previous


        val fromDate = SimpleDateFormat("yyyy-MM-dd").parse(strFromDate)
        previousCalendar.timeInMillis = fromDate.time
        previousCalendar.add(Calendar.DAY_OF_MONTH, -30)


        val minDate = previousCalendar.timeInMillis


        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        datePickerDialog.datePicker.minDate = minDate

        // Show the date picker dialog
        datePickerDialog.show()
    }

    override fun onClick(view: View?) {

        when(view?.id){

            binding.etFromDate.id -> {
                fromDatePicker()
            }
            binding.etToDate.id -> {

                if(binding.etFromDate.text.toString().isNotEmpty()){
                    toDatePicker(binding.etFromDate.text.toString())
                }
                else{
                    showSnackBar(binding.root,"Please Select From Date First")
                }

            }
            binding.imgClose.id ->{

                this@CalanderActivity.finish()
            }

        }
    }

}