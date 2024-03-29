package com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.CustomTabPageAdapter
import com.policyboss.demoandroidapp.databinding.ActivityCustomTabLayout2Binding
import com.policyboss.demoandroidapp.databinding.ActivityCustomTabLayoutBinding

// Link : custom https://www.youtube.com/watch?v=pKUZKpVPxi8

//https://www.youtube.com/watch?v=g3FCfdcv64w&list=PLArxYtt4apKsKxe-F23wv6FeJ8Al-iaV8&index=2

class CustomTabLayout2Activity : AppCompatActivity() {

    lateinit var binding : ActivityCustomTabLayout2Binding

    lateinit var tabLayout : TabLayout
    lateinit var viewpager : ViewPager2
    lateinit var adapter : CustomTabPageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomTabLayout2Binding.inflate(layoutInflater)

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