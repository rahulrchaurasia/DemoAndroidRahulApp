package com.policyboss.demoandroidapp.UI.CustomSpinner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.CustomSpinner
import com.policyboss.demoandroidapp.Utility.showToast
import com.policyboss.demoandroidapp.databinding.ActivityCustomSpinnerBinding
import com.policyboss.demoandroidapp.databinding.CustomSpinnerLayoutBinding
import com.policyboss.policybosspro.login.customSpinner.SpinnerItem
import com.policyboss.policybosspro.login.customSpinnerAdapter.CustomSpinnerAdapter

class CustomSpinnerActivity : AppCompatActivity() , CustomSpinner.OnSpinnerEventsListener{

    lateinit var binding: ActivityCustomSpinnerBinding
    lateinit var customSpinner: CustomSpinner

    val items = listOf(
        SpinnerItem(0,"Select Login Type"),
        SpinnerItem(1,"Login Via Password"),
        SpinnerItem(2,"Login Via OTP")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_spinner)

        binding = ActivityCustomSpinnerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoginSpinner()
    }

    fun setLoginSpinner() {

        // Create an ArrayAdapter with your items
        val adapter = CustomSpinnerAdapter(this, items)

        customSpinner.adapter = adapter

        customSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle item selection

                    if(position != 0){

                        val selectedItem : SpinnerItem = items[position]
                        //val selectedView = view as? TextView
                       // setEnableNextButton(true)
                        showToast(selectedItem.name.toString())
                    }else{
                       //setEnableNextButton(false)
                    }



                    //  val selectedView = view as? TextView
//                selectedView?.setTextColor(Color.WHITE)
//                selectedView?.setBackgroundResource(R.drawable.selected_spinner_background)

                    // showAlert(selectedView.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle when nothing is selected
                }

            }
    }

    override fun onPopupWindowOpened(spinner: Spinner?) {
        customSpinner.setBackgroundResource(R.drawable.bg_spinner_down)
    }

    override fun onPopupWindowClosed(spinner: Spinner?) {
        customSpinner.setBackgroundResource(R.drawable.bg_spinner_up)
    }
}