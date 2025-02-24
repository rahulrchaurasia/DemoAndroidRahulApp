package com.policyboss.demoandroidapp.UI.Collapsing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.UI.RecyclerViewOperation.RecycleMultiViewTypeActivity
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarLayoutBinding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarMainBinding
import com.policyboss.demoandroidapp.databinding.ActivityRecyclerViewMainBinding

class CollapsingToolbarMain : BaseActivity() , View.OnClickListener{

    lateinit var binding: ActivityCollapsingToolbarMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCollapsingToolbarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Collapsing Demo"


        }

        setClickListener()

       // onBackPressedDispatcher.addCallback(this, callback)

    }

    fun setClickListener() {

        binding.btnRecycle1.setOnClickListener(this)

        binding.btnRecycle2.setOnClickListener(this)

        binding.btnRecycle3.setOnClickListener(this)

        binding.btnRecycle4.setOnClickListener(this)

        binding.btnRecycle5.setOnClickListener(this)

        binding.btnRecycle6.setOnClickListener(this)

        binding.btnRecycle7.setOnClickListener(this)

        binding.btnRecycle8.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {

            binding.btnRecycle1.id -> {

                startActivity(Intent(this, BasicCollapsingActivity::class.java))

            }

            binding.btnRecycle2.id -> {

                startActivity(Intent(this, CollapsingToolbarLayoutActivity::class.java))


            }

            binding.btnRecycle3.id -> {

                startActivity(Intent(this, CollapsingWithPinnedActivity::class.java))

            }
            binding.btnRecycle4.id -> {

                startActivity(Intent(this, CollapsingToolbarWithPinnedActivity::class.java))

            }

            binding.btnRecycle5.id -> {


                startActivity(Intent(this, CollapsingDemo4Activity::class.java))


            }
            binding.btnRecycle6.id -> {

                startActivity(Intent(this, CollapsingDemo5Activity::class.java))

            }

            binding.btnRecycle7.id -> {

                startActivity(Intent(this, CollapsingDemo6Activity::class.java))

            }
            binding.btnRecycle8.id -> {

                startActivity(Intent(this, CollapsingDemo7Activity::class.java))

            }


        }
    }
}