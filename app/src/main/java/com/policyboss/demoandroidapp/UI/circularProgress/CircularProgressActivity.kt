package com.policyboss.demoandroidapp.UI.circularProgress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.DateMaskFormat
import com.policyboss.demoandroidapp.databinding.ActivityCircularProgressBinding
import com.policyboss.demoandroidapp.databinding.ActivityMaterialEditTextDemoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CircularProgressActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding  : ActivityCircularProgressBinding

    private var progressJob: Job? = null // Store the Job reference for cancellation

    private lateinit var progressIndicator: CircularProgressIndicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_circular_progress)
        binding = ActivityCircularProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressIndicator = binding.circularProgressBar
        progressIndicator.progress = 0


         setOnClickListner()
    }

    fun setOnClickListner(){


        binding.ivClose.setOnClickListener(this)
        binding.btnStart.setOnClickListener(this)


    }



    private fun cancelProgressAnimation() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun startProgressAnimation() {
        // Cancel previous job if already running
        cancelProgressAnimation()

        binding.txtResult.text =""
        progressJob = lifecycleScope.launch(Dispatchers.Main) {

            for(progress in 0..100 step 1){

                if(isActive){
                    withContext(Dispatchers.Main){
                        progressIndicator.progress = progress
                    }
                }
                delay(50)
            }
            // Switch to next ViewPager item if the job isn't cancelled
            if (isActive) {
                withContext(Dispatchers.Main) {
                   // viewPager.setCurrentItem(viewPager.currentItem + 1, false)
                    binding.txtResult.text = "Done, Successfully....!!"
                }
            }
        }
    }


    override fun onClick(view: View?) {
        when(view?.id){

            binding.ivClose.id -> {

                this.finish()
                //showAlert(binding.etDob.text.toString())
            }

            binding.btnStart.id ->{

                startProgressAnimation()

            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        cancelProgressAnimation() // Cancel the job on activity/fragment destruction

    }
}