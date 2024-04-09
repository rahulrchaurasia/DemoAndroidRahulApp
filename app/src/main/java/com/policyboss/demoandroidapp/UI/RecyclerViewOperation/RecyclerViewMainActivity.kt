package com.policyboss.demoandroidapp.UI.RecyclerViewOperation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.ViewPagerDemo.BasicViewPager.BasicViewPagerActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.CarouselViewPager.CarouselViewPagerActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI.CustomTabLayout2Activity
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI.CustomTabLayoutActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.TabLayout.CustomTabLayout.UI.ScrollableTabLayoutActivity
import com.policyboss.demoandroidapp.ViewPagerDemo.ViewPagerWithProgress.UI.ViewPagerWithProgressActivity
import com.policyboss.demoandroidapp.databinding.ActivityRecyclerViewMainBinding
import com.policyboss.demoandroidapp.databinding.ActivityViewPagerMainBinding

class RecyclerViewMainActivity : BaseActivity() , View.OnClickListener{


    lateinit var binding: ActivityRecyclerViewMainBinding


    private val callback = object : OnBackPressedCallback(enabled = true) {
        override fun handleOnBackPressed() {
            this@RecyclerViewMainActivity.finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRecyclerViewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "RecyclerView Demo"


        }

        setClickListener()

        onBackPressedDispatcher.addCallback(this, callback)

    }

    fun setClickListener() {

        binding.btnRecycle1.setOnClickListener(this)

        binding.btnRecycle2.setOnClickListener(this)

        binding.btnRecycle3.setOnClickListener(this)

        binding.btnRecycle4.setOnClickListener(this)

        binding.btnRecycle5.setOnClickListener(this)

        binding.btnRecycle6.setOnClickListener(this)

        binding.btnRecycle7.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnRecycle1.id -> {

                startActivity(Intent(this, RecycleMultiViewTypeActivity::class.java))

            }

            binding.btnRecycle2.id -> {



            }

            binding.btnRecycle3.id -> {

                           }
            binding.btnRecycle4.id -> {

                 }

            binding.btnRecycle5.id -> {

                            }
            binding.btnRecycle6.id -> {


            }

            binding.btnRecycle7.id -> {

                //  startActivity(Intent(this, CustomTabLayoutActivity::class.java))

            }

        }
    }
}