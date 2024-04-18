package com.policyboss.demoandroidapp.UI.Collapsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarWithPinnedBinding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingWithPinnedBinding

// Note : CoordinatorLayout always work like frame / Relative latout  ie each layout top of other-->
class CollapsingToolbarWithPinnedActivity : AppCompatActivity() {

    lateinit var binding: ActivityCollapsingToolbarWithPinnedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapsing_toolbar_with_pinned)

        binding = ActivityCollapsingToolbarWithPinnedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Toolbar with Collpse Child "


        }
    }
}