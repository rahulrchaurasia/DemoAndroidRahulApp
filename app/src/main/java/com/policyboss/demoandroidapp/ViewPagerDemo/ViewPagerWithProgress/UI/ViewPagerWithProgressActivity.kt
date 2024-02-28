package com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.MaterialEditText.MyPagerAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter.ViewPagerWithProgressAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter.ViewPager_ProgressAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ActivityMaterialEditTextDemoBinding
import com.policyboss.demoandroidapp.databinding.ActivityViewPagerWithProgressBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewPagerWithProgressActivity : AppCompatActivity() {

    lateinit var binding : ActivityViewPagerWithProgressBinding

    var foodList : MutableList<FoodEntity> = ArrayList<FoodEntity>()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapterViewPager : ViewPagerWithProgressAdapter

    private lateinit var adpaterProgress : ViewPager_ProgressAdapter

    private var isUserScrolling = false

//    private lateinit var progressIndicator: LinearProgressIndicator

    private val autoScrollJob: Job by lazy {

        lifecycleScope.launch(Dispatchers.Main){

            while (true) {
                delay(5000) // Adjust the delay as needed (e.g., 5000 milliseconds for 5 seconds)
                withContext(Dispatchers.Main) {
                    //Note : not exceed the last item it agian reset to first

                    if (!isUserScrolling) {
                        viewPager.currentItem = (viewPager.currentItem + 1) % viewPager.adapter?.itemCount!!
                    }
                }
            }

        }
        // The launch function actually returns a Job, so we return it here

    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            // Update the progress animations based on the currently visible item
            val foodEntity : FoodEntity =  foodList[position]
          //  foodEntity.isUpdate = true

            adpaterProgress.updateProgressAnimations(foodEntity)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            // Check if the user is scrolling manually
            isUserScrolling = state == ViewPager2.SCROLL_STATE_DRAGGING
            Log.d(Constant.TAG,"User Scroll : $isUserScrolling")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerWithProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getfoodList()
        init()
        // Start auto-scrolling when the activity is created
        autoScrollJob.start()
    }
    fun  init() {



        viewPager = binding.viewPager

        viewPager.offscreenPageLimit = 1
        viewPager.clipChildren = false
        viewPager.clipToPadding = false
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        adapterViewPager = ViewPagerWithProgressAdapter(context = this, list = foodList){ pos ->

        }
        viewPager.adapter = adapterViewPager



        viewPager.registerOnPageChangeCallback(onPageChangeCallback)

        setProgressAdapter()


    }

    fun getfoodList(){

        foodList = mutableListOf(
            FoodEntity(1, "This perfectly thin cut just melts in your mouth.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/asian-flank-steak.jpg",0.20f,"Tandoor") ,
            FoodEntity(2, "Seasoned shrimp from the depths of the Atlantic Ocean.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/blackened-shrimp.jpg",0.80f, "Prawns") ,
            FoodEntity(3, "The tasty bites of chicken have just the right amount of kick to them.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/buffalo-chicken-bites.jpg",0.10f,"Chicken Wings"),
            //FoodEntity(4, "It's really hard to keep coming up with these descriptions.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/chicken-dumplings.jpg",0.55f)


        )
    }

    fun setProgressAdapter() {

        val layoutManager = GridLayoutManager(this, 3) // 3 columns
        binding.rvProgress.layoutManager = layoutManager

        adpaterProgress = ViewPager_ProgressAdapter(context = this, list = foodList)

        binding.rvProgress.adapter = adpaterProgress
        binding.rvProgress.setHasFixedSize(true);
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel the auto-scroll job to avoid memory leaks
        autoScrollJob.cancel()
    }
}