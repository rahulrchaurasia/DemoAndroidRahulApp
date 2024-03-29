package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.CustomTabPageAdapter
import com.policyboss.demoandroidapp.databinding.ActivityCustomTabLayoutBinding

/*******************************************************
 *
 * //for Handling Tab Indicator : Color
 * //   app:tabIndicator="@drawable/tab_indicator"
 * //   app:tabIndicatorHeight="10dp"
 * //  app:tabIndicatorColor="@null"
 *
 * 1> app:tabIndicator="@drawable/tab_indicator" we used for custom
 * if we not used tabIndicator than basic one come ____ based on tabIndicatorHeight
 *
 * 2>   app:tabIndicatorGravity="stretch" and center
 *  for showing position of tabIndicator
 *******************************************************/

class CustomTabLayoutActivity : AppCompatActivity() {

    lateinit var binding : ActivityCustomTabLayoutBinding

    lateinit var tabLayout : TabLayout
    lateinit var viewpager : ViewPager2
    lateinit var adapter : CustomTabPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomTabLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
        setTabListener()   //for TabListener

        viewpagerSetPageCallBack() //viewpager Listener
    }

    fun init(){

        tabLayout = binding.tabLayout
        viewpager = binding.viewpager2

        adapter = CustomTabPageAdapter(supportFragmentManager,lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("First"))
        tabLayout.addTab(tabLayout.newTab().setText("Second"))

        viewpager.adapter = adapter
    }

    fun setTabListener(){

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                if(tab != null){
                    viewpager.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }


        } )
    }


    fun viewpagerSetPageCallBack(){

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                tabLayout.selectTab(tabLayout.getTabAt(position))
            }

        })
    }
}