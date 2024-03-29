package com.policyboss.demoandroidapp.ViewPagerDemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.BasicViewPager.BasicViewPagerActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.CarouselViewPager.CarouselViewPagerActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI.CustomTabLayout2Activity
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI.CustomTabLayoutActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI.ScrollableTabLayoutActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.UI.ViewPagerWithProgressActivity
import com.policyboss.demoandroidapp.databinding.ActivityViewPagerMainBinding

class ViewPagerMainActivity : BaseActivity() , View.OnClickListener{

    lateinit var binding: ActivityViewPagerMainBinding

    private val callback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            this@ViewPagerMainActivity.finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_view_pager_main)

        binding = ActivityViewPagerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "ViewPager Demo"


        }


        setClickListener()

        onBackPressedDispatcher.addCallback(this, callback)

    }

    fun setClickListener() {

        binding.btnViePager1.setOnClickListener(this)

        binding.btnViePager2.setOnClickListener(this)

        binding.btnViePager3.setOnClickListener(this)

        binding.btnViePager4.setOnClickListener(this)

        binding.btnViePager5.setOnClickListener(this)

        binding.btnViePager6.setOnClickListener(this)

        binding.btnViePager7.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnViePager1.id -> {

                startActivity(Intent(this, BasicViewPagerActivity::class.java))

            }

            binding.btnViePager2.id -> {



                val intent = Intent(this, ViewPagerWithProgressActivity::class.java).apply {
                    putExtra("message", "Hello, TargetActivity!")
                }

                startActivity(intent)
            }

            binding.btnViePager3.id -> {

                startActivity(Intent(this, CarouselViewPagerActivity::class.java))
            }
            binding.btnViePager4.id -> {

                startActivity(Intent(this, CustomTabLayoutActivity::class.java))

            }

            binding.btnViePager5.id -> {

                startActivity(Intent(this, CustomTabLayout2Activity::class.java))
            }
            binding.btnViePager6.id -> {

                startActivity(Intent(this, ScrollableTabLayoutActivity::class.java))

            }

            binding.btnViePager7.id -> {

              //  startActivity(Intent(this, CustomTabLayoutActivity::class.java))

            }

        }
    }
}