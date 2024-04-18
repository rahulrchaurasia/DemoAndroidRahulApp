package com.policyboss.demoandroidapp.UI.Collapsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo4Binding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarWithPinnedBinding

// Note : CoordinatorLayout always work like frame / Relative latout  ie each layout top of other-->
class CollapsingDemo4Activity : AppCompatActivity() {

    lateinit var binding: ActivityCollapsingDemo4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollapsingDemo4Binding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.let {
//
//            it.setDisplayHomeAsUpEnabled(true)
//            it.setDisplayShowHomeEnabled(true)
//            it.title = "CollapsingDemo Toolbar Demo  "
//
//
//        }
    }
}