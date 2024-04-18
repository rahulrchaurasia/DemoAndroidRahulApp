package com.policyboss.demoandroidapp.UI.Collapsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarMainBinding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingWithPinnedBinding


//Note :  app:layout_scrollFlags="scroll|enterAlways" if we used this its fully scrolled
// the appbar layout and layout which is above nested Scroll View is always Pinned by default.
class CollapsingWithPinnedActivity : AppCompatActivity() {

    lateinit var binding: ActivityCollapsingWithPinnedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollapsingWithPinnedBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.let {
//
//            it.setDisplayHomeAsUpEnabled(true)
//            it.setDisplayShowHomeEnabled(true)
//            it.title = "Collapsing Toolbar "
//
//
//        }

    }
}