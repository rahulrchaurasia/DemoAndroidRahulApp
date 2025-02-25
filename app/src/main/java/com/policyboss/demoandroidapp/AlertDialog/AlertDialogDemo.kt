package com.policyboss.demoandroidapp.AlertDialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.policyboss.demoandroidapp.R

import com.policyboss.demoandroidapp.Utility.CustomPopupWindow
import com.policyboss.demoandroidapp.Utility.PopupView
import com.policyboss.demoandroidapp.Utility.PopupViewFullScreen
import com.policyboss.demoandroidapp.Utility.PopupViewTop
import com.policyboss.demoandroidapp.databinding.ActivityAlertDialogDemoBinding

class AlertDialogDemo : AppCompatActivity() , View.OnClickListener{

    lateinit var binding: ActivityAlertDialogDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlertDialogDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Alert Dialog Demo"


        }

        setClickListener()
    }

    fun setClickListener() {


        binding.btnPopOverlay.setOnClickListener(this)

        binding.btnPopOverlayTop.setOnClickListener(this)

        binding.btnTransluent.setOnClickListener(this)

        binding.btnPopUpAlert.setOnClickListener(this)

        binding.btnSimplePopUp.setOnClickListener(this)

        binding.btnCustomActivityAlert.setOnClickListener(this)
    }

   fun showPopupWindow(anchorView: View) {

       val popupView = layoutInflater.inflate(R.layout.simple_alert_dialog, null)

        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

       popupWindow.showAsDropDown(anchorView)
    }

    override fun onClick(view: View?) {
        when(view?.id) {


            binding.btnPopOverlay.id ->{

                // For FullScreen
                val popupView = PopupViewFullScreen(context = this@AlertDialogDemo)
                popupView.show()



            }

            binding.btnPopOverlayTop.id ->{

               // simple only set on Zgravity on Top
                val popupView = PopupViewTop(context = this@AlertDialogDemo)
                popupView.show()


            }
            binding.btnTransluent.id ->{


                val popupView = PopupView(context = this@AlertDialogDemo)
                popupView.show()


            }
            binding.btnPopUpAlert.id ->{


                val popupView = CustomPopupWindow(context = this@AlertDialogDemo)

                popupView.show(anchorView = binding.menuButton, message = "this is menu please click here")

                //popupView.show(anchorView = binding.btnPopOverlay, message = "this is menu please click here")


            }
            //CustomAlertDialog
            binding.btnSimplePopUp.id -> {


                showPopupWindow(anchorView = binding.menuButton)
            }

            binding.btnCustomActivityAlert.id ->{

                startActivity(Intent(this, customActivityAsDialog::class.java))

            }
        }
    }


}