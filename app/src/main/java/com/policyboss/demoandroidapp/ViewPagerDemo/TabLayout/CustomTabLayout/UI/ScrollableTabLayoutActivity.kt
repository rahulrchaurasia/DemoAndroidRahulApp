package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.CustomTabPageAdapter
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.ScrollableTabPageAdapter
import com.policyboss.demoandroidapp.databinding.ActivityCustomTabLayoutBinding
import com.policyboss.demoandroidapp.databinding.ActivityScrollableTabLayoutBinding

class ScrollableTabLayoutActivity : AppCompatActivity() {

    lateinit var binding : ActivityScrollableTabLayoutBinding

    lateinit var tabLayout : TabLayout
    lateinit var viewpager : ViewPager2
    lateinit var adapter : ScrollableTabPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollableTabLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()

        setTabListener()   //for TabListener

        viewpagerSetPageCallBack() //viewpager Listener
    }

    fun init(){

        tabLayout = binding.tabLayout
        viewpager = binding.viewpager2

        adapter = ScrollableTabPageAdapter(supportFragmentManager,lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("First"))
        tabLayout.addTab(tabLayout.newTab().setText("Second"))
        tabLayout.addTab(tabLayout.newTab().setText("Third"))
        tabLayout.addTab(tabLayout.newTab().setText("Four"))
        tabLayout.addTab(tabLayout.newTab().setText("five"))
        tabLayout.addTab(tabLayout.newTab().setText("six"))

        viewpager.adapter = adapter
    }

    fun setTabListener(){

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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