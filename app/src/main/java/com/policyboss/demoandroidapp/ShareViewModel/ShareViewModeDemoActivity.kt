package com.policyboss.demoandroidapp.ShareViewModel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.google.android.material.snackbar.Snackbar
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ShareViewModel.fragment.TestFragment1
import com.policyboss.demoandroidapp.ShareViewModel.fragment.TestFragment2
import com.policyboss.demoandroidapp.databinding.ActivityShareViewModeDemoBinding

class ShareViewModeDemoActivity : AppCompatActivity() {

    lateinit var binding:ActivityShareViewModeDemoBinding

  //  private val viewModelDemo2 : ViewModelDemo2 by viewModels()   // Initialize directly

    private val viewModelDemo2 by viewModels<ViewModelDemo2>()

    lateinit var  etView  : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_share_view_mode_demo)

        binding = ActivityShareViewModeDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)


        }
        etView = binding.includeViewmodelDemo2.etData  //(Multiple time called therefore we written )


        binding.includeViewmodelDemo2.btnFrag1.setOnClickListener{



            Constant.hideKeyBoard(etView,this)

            if(validate())
            {
                viewModelDemo2.setData(binding.includeViewmodelDemo2.etData.text.toString())

                supportFragmentManager.beginTransaction()
                    .replace(binding.includeViewmodelDemo2.container.id, TestFragment1())
                    .commit()

            }


        }

        binding.includeViewmodelDemo2.btnFrag2.setOnClickListener{
            Constant.hideKeyBoard(etView,this)
            if(validate()) {
                viewModelDemo2.setData(binding.includeViewmodelDemo2.etData.text.toString())
                supportFragmentManager.beginTransaction()
                    .replace(binding.includeViewmodelDemo2.container.id, TestFragment2())
                    .commit()
            }
        }

    }
    fun validate() : Boolean{

        var blnCheck : Boolean = true
        if(binding.includeViewmodelDemo2.etData.text.isNullOrBlank()){
            Snackbar.make(etView,"Required Value!!", Snackbar.LENGTH_SHORT).show()
            blnCheck =  false
        }
        return  blnCheck
    }
}
