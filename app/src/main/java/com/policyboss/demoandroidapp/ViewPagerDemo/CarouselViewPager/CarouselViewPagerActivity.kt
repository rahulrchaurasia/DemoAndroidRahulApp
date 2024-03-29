package com.policyboss.demoandroidapp.ViewPagerDemo.CarouselViewPager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.demo.kotlindemoapp.HomeMain.CarouselViewPager.CarouselTransformer
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardEntity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.BasicViewPager.BasicViewPagerAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.CarouselViewPager.DotIndicator.DottedViewPagerAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.adapter.ViewPager_LinearProgressAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.model.DotIdicatorEntity
import com.policyboss.demoandroidapp.ViewPagerDemo.model.FoodEntity
import com.policyboss.demoandroidapp.databinding.ActivityBasicViewPagerBinding
import com.policyboss.demoandroidapp.databinding.ActivityCarouselViewPagerBinding

class CarouselViewPagerActivity : AppCompatActivity() {

    lateinit var binding : ActivityCarouselViewPagerBinding

    var foodList : MutableList<FoodEntity> = ArrayList<FoodEntity>()

    var dottedList : MutableList<DotIdicatorEntity> = ArrayList<DotIdicatorEntity>()

    private lateinit var viewPager: ViewPager2
    private lateinit var handler: Handler

    private lateinit var adapterViewPager : CarouselViewPagerAdapter

    private lateinit var dottedAdapter : DottedViewPagerAdapter

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback(){


        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            Log.d(Constant.TAG,"onPageSelected : $position " )



                val foodEntity : FoodEntity =  foodList[position]
                // adpaterLinearProg.updateProgressAnimations(foodEntity)
                foodEntity?.let { // Update progress only if item exists

                    if(dottedList.size > 0){
                        dottedAdapter.updateDotList(it)
                    }

                }

            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable,2000)



        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarouselViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getfoodList()

        dottedList = getDotList(foodList)
        init()
        binding.ivClose.setOnClickListener{

            this@CarouselViewPagerActivity.finish()
        }
    }

    fun  init() {

        handler = Handler(Looper.myLooper()!!)
        viewPager = binding.viewPager


        setDottedAdapter()
        setupCarousel()

       // binding.dot1.attachTo(viewPager)


    }

     fun setupCarousel(){

         viewPager.offscreenPageLimit = 3
         viewPager.clipChildren = false
         viewPager.clipToPadding = false
         viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
         viewPager.setPageTransformer(CarouselTransformer(this))

         adapterViewPager = CarouselViewPagerAdapter(context = this, list = foodList){ entity ,pos ->

         }

         // set Adapter
         viewPager.adapter = adapterViewPager


         viewPager.registerOnPageChangeCallback(onPageChangeCallback)


     }

    fun setDottedAdapter() {

      //  val layoutManager = GridLayoutManager(this, 3) // 3 columns
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.rvDot.layoutManager = layoutManager

        dottedAdapter = DottedViewPagerAdapter(context = this, list = dottedList){

        }

        binding.rvDot.adapter = dottedAdapter
        binding.rvDot.setHasFixedSize(true)
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


    fun getDotList(foodList: List<FoodEntity>): MutableList<DotIdicatorEntity> {
        return foodList.map { foodEntity ->
            DotIdicatorEntity(foodEntity.id, R.drawable.unselected_dot, isSelected = false)
        }.toMutableList()
    }



//    private fun updateDotIndicators(position: Int) {
//        dots.forEachIndexed { index, imageView ->
//            imageView.setImageResource(
//                if (index == position) R.drawable.indicator_active else R.drawable.unselected_dot
//            )
//        }
//    }

    private val runnable = Runnable {

        viewPager.currentItem = viewPager.currentItem + 1
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,3000)

    }
    override fun onDestroy() {
        super.onDestroy()

        adapterViewPager.cancelJob()

    }
}