package com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter.ViewPagerWithProgressAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter.ViewPager_LinearProgressAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ActivityViewPagerWithProgressBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//*************************************************************************
//Note :

//1> we have to match timming of Activity and Adapter same here we take 5 sec

//2>  The issue you're facing might be related to the fact that a canceled Job cannot be restarted.
// Once a Job is canceled, it cannot be used again. To achieve the desired behavior of
// stopping and restarting the auto-scroll functionality, you can create a new Job instance
// when restarting the auto-scroll.

//3>  we get diff pos in onPageScrollStateChanged and onPageSelected

//onPageScrollStateChanged uses viewPager.currentItem to access the foodList item, which might not be updated yet during scrolling.
//onPageSelected uses the provided position which reflects the final, selected item after scrolling.
//*************************************************************************

class ViewPagerWithProgressActivity : AppCompatActivity() {

    lateinit var binding : ActivityViewPagerWithProgressBinding

    var foodList : MutableList<FoodEntity> = ArrayList<FoodEntity>()
    private lateinit var viewPager: ViewPager2
    private lateinit var adapterViewPager : ViewPagerWithProgressAdapter

    private lateinit var adpaterLinearProg : ViewPager_LinearProgressAdapter

    private var isUserScrolling = false

    private var isAutoUpdateRestart = false

//    private lateinit var progressIndicator: LinearProgressIndicator


    private var autoScrollJob: Job? = null  // Make autoScrollJob nullable

    private var lastUpdatedPosition = -1

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){


        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
           //Note : uses the provided position which reflects the final, selected item after scrolling
            if (!isUserScrolling) {


               // adpaterLinearProg.updateProgressAnimations(foodList.get( viewPager.currentItem))
               if(!isAutoUpdateRestart) {
                   val foodEntity : FoodEntity =  foodList[position]
                   // adpaterLinearProg.updateProgressAnimations(foodEntity)
                   foodEntity?.let { // Update progress only if item exists
                       adpaterLinearProg.updateProgressAnimations(it)
                   }
               }else{
                   isAutoUpdateRestart = false

                  // startAutoScroll(startPosition =  viewPager.currentItem,_restartImmediate = true)
//                   startAutoScroll(startPosition = position,_restartImmediate = true)
//                   adpaterLinearProg.cancelProgressAnimation()

               }

            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
     //onPageScrollStateChanged uses viewPager.currentItem to access the foodList item which might not be updated yet during scrolling.
            when (state) {
                ViewPager2.SCROLL_STATE_DRAGGING -> {
                    // User started scrolling
                    isUserScrolling = true
                    stopAutoScroll()
                    adpaterLinearProg.cancelProgressAnimation()
                    Log.d(Constant.TAG,"User Scroll : $isUserScrolling cancel Auto Scroll Linear-Progress" )

                }
                ViewPager2.SCROLL_STATE_IDLE -> {
                    // User finished scrolling
                    isUserScrolling = false

                    if (autoScrollJob?.isActive != true){


                        startAutoScroll(startPosition =  viewPager.currentItem,_restartImmediate = true)
                        //adpaterLinearProg.cancelProgressAnimation()

                        isAutoUpdateRestart = true

                      //  adpaterLinearProg.cancelProgressAnimation()
                        adpaterLinearProg.updateProgressAnimations(foodList.get( viewPager.currentItem))

                    }
//                    val currentPosition = viewPager.currentItem
//                    val foodEntity: FoodEntity = foodList[currentPosition]

                    // startAutoScroll()
                  //  adpaterLinearProg.updateProgressAnimations(foodList.get( viewPager.currentItem))
                    Log.d(Constant.TAG,"User Scroll : $isUserScrolling  activate User scroll and Linear-Progress")

                }
            }

        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerWithProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getfoodList()
        init()
        // Start auto-scrolling when the activity is created
        startAutoScroll()
         //adpaterLinearProg.updateProgressAnimations(foodList.first())

//       binding.btnToggle.setOnClickListener{
//
//          stopAutoScroll()
//           adpaterLinearProg.cancelProgressAnimation()
//
//       }
//
//        binding.btnStart.setOnClickListener{
//
//            startAutoScroll()
//
//
//        }
    }
    fun  init() {



        viewPager = binding.viewPager

        viewPager.offscreenPageLimit = 1
        viewPager.clipChildren = false
        viewPager.clipToPadding = false
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        adapterViewPager = ViewPagerWithProgressAdapter(context = this, list = foodList){ entity ,pos ->



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


    private fun  startAutoScroll( startPosition: Int = -1, _restartImmediate : Boolean = false) {


         var restartImmediate = _restartImmediate
        // The launch function actually returns a Job, so we return it here

        if ( autoScrollJob?.isActive == true) {

           // stopAutoScroll()
            return
        }
        stopAutoScroll()

        autoScrollJob = lifecycleScope.launch(Dispatchers.Main) {

                while (isActive) {
                    // Skip delay only if restartImmediate is true
                    if (restartImmediate) {

                        restartImmediate = false
                    } else {
                        delay(5000) //  5 sec Adjust the delay for your needs
                    }

                    withContext(Dispatchers.Main) {

                        // viewPager.currentItem = (viewPager.currentItem + 1) % viewPager.adapter?.itemCount!!

                        if (viewPager.currentItem < viewPager.adapter?.itemCount!! - 1) {
                            viewPager.currentItem++
                        } else {
                            viewPager.currentItem = 0
                        }
//                        if (restartImmediate) {
//                            viewPager.currentItem = startPosition
//
//                        }else{
//
//                            if (viewPager.currentItem < viewPager.adapter?.itemCount!! - 1) {
//                                viewPager.currentItem++
//                            } else {
//                                viewPager.currentItem = 0
//                            }
//                        }

                        restartImmediate = false





                    }
                }
            }


    }


    fun setProgressAdapter() {

        val layoutManager = GridLayoutManager(this, 3) // 3 columns
        binding.rvProgress.layoutManager = layoutManager

        adpaterLinearProg = ViewPager_LinearProgressAdapter(context = this, list = foodList)

        binding.rvProgress.adapter = adpaterLinearProg
        binding.rvProgress.setHasFixedSize(true);
    }

    fun stopAutoScroll() {
        autoScrollJob?.cancel()

    }
    override fun onDestroy() {
        super.onDestroy()
        // Cancel the auto-scroll job to avoid memory leaks
        stopAutoScroll()
    }
}