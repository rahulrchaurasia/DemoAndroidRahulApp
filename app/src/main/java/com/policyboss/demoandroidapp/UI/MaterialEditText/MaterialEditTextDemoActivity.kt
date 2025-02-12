package com.policyboss.demoandroidapp.UI.MaterialEditText

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.Utility.DateMaskFormat

import com.policyboss.demoandroidapp.Utility.ExtensionFun.addPlateNumberFormatter
import com.policyboss.demoandroidapp.Utility.ExtensionFun.isValidDate
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ActivityMaterialEditTextDemoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//Note :SsnMask - Give Format For EdiTTexthere dd-mm-yyyy
//link https://brandonlehr.com/android/learn-to-code/2018/08/20/android-edit-text-input-masking

/*
For Default textinputEdit Color add color
             <color name="mtrl_textinput_default_box_stroke_color" tools:override="true">#B1E872</color>

 */

class MaterialEditTextDemoActivity : BaseActivity() , View.OnClickListener {


    lateinit var binding : ActivityMaterialEditTextDemoBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter : MyPagerAdapter
    private lateinit var progressIndicator: LinearProgressIndicator

    private var progressJob: Job? = null // Store the Job reference for cancellation


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_material_edit_text_demo)

        binding = ActivityMaterialEditTextDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setOnClickListner()
        progressIndicator.progress = 0



        // Handle page changes and show/hide progress bar
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//
//
//                // Reset progress bar before starting new animation
//                adapter.cancelProgressAnimation() // Cancel ongoing animation, if any
//                progressIndicator?.progress = 0
//
//                // Start animation for the newly selected item
//              val  view =  (viewPager.getChildAt(position))
//
//            }
//        })

    }

    fun setOnClickListner(){


        binding.ivClose.setOnClickListener(this)

        //region Closure directly use
        //binding.etDob.addTextChangedListener(SsnMask())
//        val dateMask = DateMaskFormat(separator = "-"){
//            showAlert(it)
//           var age =  Utility.calculateAge(dob = it, customDateFormat = "DD-MM-YYYY")
//
//           binding.tvAge.text = "${age} years"
//        }
        //endregion
        val dateMask = DateMaskFormat(separator = "-", binding.etDob,::getDateMask)

        binding.etDob.addTextChangedListener(dateMask)


        binding.etPlate.addPlateNumberFormatter()



    }
    fun  init() {



        viewPager = binding.viewPager
        progressIndicator = binding.progressBar

        viewPager.offscreenPageLimit = 1
        viewPager.clipChildren = false
        viewPager.clipToPadding = false
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val foodList = mutableListOf(
            FoodEntity(1, "This perfectly thin cut just melts in your mouth.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/asian-flank-steak.jpg") ,
            FoodEntity(2, "Seasoned shrimp from the depths of the Atlantic Ocean.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/blackened-shrimp.jpg") ,
            FoodEntity(3, "The tasty bites of chicken have just the right amount of kick to them.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/buffalo-chicken-bites.jpg"),
            FoodEntity(4, "It's really hard to keep coming up with these descriptions.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/chicken-dumplings.jpg")


        )
        adapter = MyPagerAdapter(context = this, viewPager = viewPager,list = foodList){ pos ->


//            if (pos == foodList.lastIndex) {
//                viewPager.setCurrentItem(0, false) // Loop back to first item
//            } else {
//                viewPager.setCurrentItem(pos + 1, false)
//            }

        }
        viewPager.adapter = adapter




    }

    private fun startProgressAnimation() {
        // Cancel previous job if already running
        cancelProgressAnimation()

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
                    viewPager.setCurrentItem(viewPager.currentItem + 1, false)
                }
            }
        }
    }

    private fun cancelProgressAnimation() {
        progressJob?.cancel()
        progressJob = null
    }


    fun getDateMask(strData : String){

        if(strData.isValidDate()){

           // showAlert(strData)
            var age =  Utility.calculateAge(dob = strData, customDateFormat = "DD-MM-YYYY")
            binding.tvAge.visibility = View.VISIBLE
            binding.tvAge.text = "${age} years"
        }else{
            binding.tvAge.visibility = View.INVISIBLE
          //  binding.etDob.setText("")
            showAlert("Invalidate Date Format")
        }

    }



    override fun onClick(view: View?) {

        when(view?.id){

            binding.ivClose.id -> {

                this.finish()
                //showAlert(binding.etDob.text.toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelProgressAnimation() // Cancel the job on activity/fragment destruction

    }
}