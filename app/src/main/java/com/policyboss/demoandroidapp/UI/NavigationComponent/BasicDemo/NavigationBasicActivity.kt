package com.policyboss.demoandroidapp.UI.NavigationComponent.BasicDemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityNavigationBasicBinding


class NavigationBasicActivity : AppCompatActivity() {

    lateinit var binding: ActivityNavigationBasicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBasicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Navigation Component"
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            androidx.appcompat.R.id.home -> {
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {

        finish()
    }
}