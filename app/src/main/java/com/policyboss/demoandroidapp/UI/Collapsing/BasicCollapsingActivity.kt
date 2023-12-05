package com.policyboss.demoandroidapp.UI.Collapsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.Utility.showToast
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
/*2
 android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:contentScrim="@color/green"
            app:layout_scrollFlags="scroll|snap|exitUntilCollapsed"
 */

// 3 : In CollapsingToolbarLayout app:titleEnabled="false"   set the title by default position ie at top
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

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // Check if the CollapsingToolbarLayout is completely collapsed
            val isCollapsed = Math.abs(verticalOffset) == appBarLayout.totalScrollRange

            // Update Toolbar appearance based on collapse state
            if ( isCollapsed) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                //binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.pureBlack))
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
               // binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.collpase_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            R.id.menu_info -> {
                showToast("Menu Click")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}