package com.policyboss.demoandroidapp.UI.Collapsing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarLayoutBinding
import com.policyboss.demoandroidapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Pinned View : ---> Based on Pinned View layout stick on top
class CollapsingToolbarLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollapsingToolbarLayoutBinding
   // private var offsetChangedListener: AppBarLayout.OnOffsetChangedListener? = null

    var  thresholdOffset = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollapsingToolbarLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)



        //region comment
//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.let {
//
//            it.setDisplayHomeAsUpEnabled(true)
//            it.setDisplayShowHomeEnabled(true)
//            it.title = "Collapsing Toolbar"
//
//
//        }
        //endregion


        // Calculate the threshold value for verticalOffset based on view heights

        binding.imgBack.visibility = View.GONE

        getCollapseToolbarHeight()
        handlingVerticalOffset()
    }




    private fun  handlingVerticalOffset(){



        binding.appBar.addOnOffsetChangedListener({ appBarLayout, verticalOffset ->



            binding.offset.text = " thresholdOffset ${thresholdOffset} verticalOffset  ${verticalOffset} "
            // Set the elevation to the pinnedLinearLayout
            // binding.pinnedLayut.elevation = elevation.toFloat()
            lifecycleScope.launch(Dispatchers.Main) {
                if (verticalOffset <=  thresholdOffset) {
                    // Vertical offset is zero, which means content is at the top

                    //delay(1000) // Introduce a 1-second delay

                    binding.pinnedLayut.elevation = 40f // Set the elevation to 40dp
                    binding.pinnedLayut.setBackgroundColor(
                        ContextCompat.getColor(
                            this@CollapsingToolbarLayoutActivity!!,
                            R.color.off_white
                        )
                    ) // Change the background color

                    binding.imgBack.visibility = View.VISIBLE


                } else  {

                    //delay(1000)
                    binding.pinnedLayut.elevation = 0f // Set the elevation to 0dp
                    binding.pinnedLayut.setBackgroundColor(
                        ContextCompat.getColor(
                            this@CollapsingToolbarLayoutActivity!!,
                            R.color.lightGrey
                        )
                    ) // Restore the original background color
                    binding.imgBack.visibility = View.GONE


                }
            }

        })

    }


    private fun getCollapseToolbarHeight(){

        // Obtain the thresholdOffset after the layout is calculated

        val viewTreeObserver = binding.appBar.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // Check if the view is still alive and in a valid state
                if (binding.appBar != null && binding.appBar.isAttachedToWindow) {

                    if (binding.appBar.isLaidOut && binding.collapsingToolbar.isLaidOut) {
                        // Check if the viewTreeObserver is attached to the appBar view

                        // Calculate the threshold value for verticalOffset based on view heights
                        thresholdOffset = -binding.collapsingToolbar.height

                        // Remove the listener to avoid multiple calls
                        binding.appBar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }


                }

            }
        })

    }

//    override fun onStop() {
//        super.onStop()
//        // Removing the listener stored as a property
//        offsetChangedListener?.let {
//            binding.appBar.removeOnOffsetChangedListener(it)
//        }
//    }
}