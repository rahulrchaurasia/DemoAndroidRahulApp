package com.policyboss.demoandroidapp.UI.Collapsing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.appbar.AppBarLayout
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.LoginModule.UI.HomeDashboardActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingToolbarLayoutBinding
import com.policyboss.demoandroidapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Pinned View : ---> Based on Pinned View layout stick on top
class CollapsingToolbarLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollapsingToolbarLayoutBinding
//    private var offsetChangedListener: AppBarLayout.OnOffsetChangedListener? = null
    private lateinit var offsetChangedListenerProperty: AppBarLayout.OnOffsetChangedListener


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
       // handlingVerticalOffset()

        //region Comment

        /*
        val offsetChangedListener = AppBarLayout.OnOffsetChangedListener({ appBarLayout, verticalOffset ->

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

        binding.appBar.addOnOffsetChangedListener(offsetChangedListener)
        Log.d(Constant.TAG,"onCreate: Added Callback")
        // Store the listener as a property of your activity
        offsetChangedListenerProperty = offsetChangedListener

         */

        //endRegion

        binding.flBaicCollpsingLy.setOnClickListener{

            startActivity(Intent(this,BasicCollapsingActivity::class.java))

        }

    }



    /*
      here we add directly OffsetChangedListener to abbBar using addOnOffsetChangedListener
    */

    // region comment
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

    //endregion


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

    override fun onStart() {
        super.onStart()

        val offsetChangedListener = AppBarLayout.OnOffsetChangedListener({ appBarLayout, verticalOffset ->

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

        binding.appBar.addOnOffsetChangedListener(offsetChangedListener)
        Log.d(Constant.TAG,"onStart: Added Callback")
        // Store the listener as a property of your activity
        offsetChangedListenerProperty = offsetChangedListener
    }

    override fun onStop() {
        super.onStop()
        // Removing the listener stored as a property
        offsetChangedListenerProperty?.let {
            binding.appBar.removeOnOffsetChangedListener(it)

            Log.d(Constant.TAG,"OnStop: Remove Callback")
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//
//        // Check if the listener is not null before removing it
//        offsetChangedListenerProperty?.let {
//            binding.appBar.removeOnOffsetChangedListener(it)
//
//            Log.d(Constant.TAG,"onDestroy: Remove Callback")
//        }
//    }
}