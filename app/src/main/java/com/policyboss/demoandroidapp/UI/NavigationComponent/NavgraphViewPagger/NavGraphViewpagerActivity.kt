package com.policyboss.demoandroidapp.UI.NavigationComponent.NavgraphViewPagger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.R

class NavGraphViewpagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_graph_viewpager)

        supportActionBar?.hide()
    }
}