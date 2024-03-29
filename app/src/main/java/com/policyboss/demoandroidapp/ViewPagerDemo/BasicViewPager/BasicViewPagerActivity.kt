package com.policyboss.demoandroidapp.ViewPagerDemo.BasicViewPager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter.ViewPagerWithProgressAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ActivityBasicViewPagerBinding
import com.policyboss.demoandroidapp.databinding.ActivityViewPagerWithProgressBinding

/**************************************************
for dot Indicator
implementation 'com.tbuonomo.andrui:viewpagerdotsindicator:3.0.3'

 ***************************************************/
class BasicViewPagerActivity : AppCompatActivity() {

    lateinit var binding : ActivityBasicViewPagerBinding

    var foodList : MutableList<FoodEntity> = ArrayList<FoodEntity>()

    private lateinit var viewPager: ViewPager2
    private lateinit var handler: Handler

    private lateinit var adapterViewPager : BasicViewPagerAdapter

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){


        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            Log.d(Constant.TAG,"onPageSelected : $position " )

//                val foodEntity : FoodEntity =  foodList[position]
//                // adpaterLinearProg.updateProgressAnimations(foodEntity)
//                foodEntity?.let { // Update progress only if item exists
//
//                }

            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable,2000)



        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBasicViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getfoodList()
        init()
        binding.ivClose.setOnClickListener{

            this@BasicViewPagerActivity.finish()
        }

         binding.dot1.attachTo(viewPager)
    }

    fun  init() {



        handler = Handler(Looper.myLooper()!!)
        viewPager = binding.viewPager

        viewPager.offscreenPageLimit = 1
        viewPager.clipChildren = false
        viewPager.clipToPadding = false
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        adapterViewPager = BasicViewPagerAdapter(context = this,list = foodList ,::setViewPager)
        viewPager.adapter = adapterViewPager



        viewPager.registerOnPageChangeCallback(onPageChangeCallback)




    }

    // bec my callback returning entity and pos of food
    fun setViewPager( entity : FoodEntity , pos : Int ){

        viewPager.setCurrentItem(0,false)
    }

    fun getfoodList(){

        foodList = mutableListOf(
            FoodEntity(1, "This perfectly thin cut just melts in your mouth.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/asian-flank-steak.jpg",0.20f,"Tandoor") ,
            FoodEntity(2, "Seasoned shrimp from the depths of the Atlantic Ocean.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/blackened-shrimp.jpg",0.80f, "Prawns") ,
            FoodEntity(3, "The tasty bites of chicken have just the right amount of kick to them.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/buffalo-chicken-bites.jpg",0.10f,"Chicken Wings"),
            FoodEntity(4, "It's really hard to keep coming up with these descriptions.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/chicken-dumplings.jpg",0.55f,"Chicken Dumplings"),

            FoodEntity(5, "This perfectly thin cut just melts in your mouth.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/mozzarella-sticks.jpg",0.20f,"Mozzarella Sticks") ,
            FoodEntity(6, "Seasoned shrimp from the depths of the Atlantic Ocean.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/philly-cheesesteak-sliders.jpg",0.80f, "Philly Cheesesteak Sliders") ,
            FoodEntity(7, "The tasty bites of chicken have just the right amount of kick to them.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/rainbow-spring-rolls.jpg",0.10f,"Rainbow Spring Roll"),
            FoodEntity(8, "It's really hard to keep coming up with these descriptions.", "https://seanallen-course-backend.herokuapp.com/images/appetizers/stuff-shells.jpg",0.55f,"Stuffed Shells")


        )
    }

    private val runnable = Runnable {

        if (viewPager != null && viewPager.adapter != null) {

            if (viewPager.currentItem < viewPager.adapter?.itemCount!! - 1) {

                var currentItem = viewPager.currentItem + 1
                viewPager.setCurrentItem(currentItem,true)
            } else {

                viewPager.setCurrentItem(0,false)
            }
        }




    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        adapterViewPager.removeCallback()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,2000)

    }
    override fun onDestroy() {
        super.onDestroy()

    }

}