package com.policyboss.demoandroidapp.UI.Collapsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityBasicCollapsingBinding

/***********************************************
<!-- In CollapsingToolbarLayout some important attributes are:
i)  app:layout_scrollFlags which is used to specify how collapsing
layout behaves when content is scrolled.I have used
app:layout_scrollFlags="scroll|snap|exitUntilCollapsed"
so it will scroll until it's is completely collapsed.
ii) app:contentScrim="@color/green" that specifies the color
of the collapsed toolbar
 *********************************************************
*/
/*
 android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:contentScrim="@color/green"
            app:layout_scrollFlags="scroll|snap|exitUntilCollapsed"
 */
class BasicCollapsingActivity : AppCompatActivity() {

    lateinit var binding : ActivityBasicCollapsingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBasicCollapsingBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.toolbar)

        // Enable back button

        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "Toolbar with Image"

        }

    }
}