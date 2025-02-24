package com.policyboss.demoandroidapp.UI.Collapsing

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.appbar.AppBarLayout
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo6Binding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo7Binding
import kotlin.math.abs

/*
For nested scrollview  handle here. Above User Coordinate Layout as root
    1> take CollapsingToolbarLayout as main in AppBarLayout layout \n
    2> Make two child of CollapsingToolbarLayout a> Expanded Content and  2> material.appbar.MaterialToolbar
    3> a> Expanded Content : use app:layout_scrollFlags="scroll|exitUntilCollapsed|snap"
    4> b> material.appbar.MaterialToolbar : user material toolbar with app:layout_collapseMode="pin"
 */

/*
Scroll Flags: The app:layout_scrollFlags="scroll|enterAlways" attribute allows the TextView to remain visible while scrolling.
The enterAlways flag ensures that the TextView will reappear when scrolling up.
 */

class CollapsingDemo7Activity : AppCompatActivity() {

    lateinit var binding: ActivityCollapsingDemo7Binding
    private var isToolbarCollapsed = false

    // Sample data for demonstration
    private val productTitle = "Burger"
    private val productPrice = "20Rs"
    private val productOriginalPrice = "30Rs"
    private val productDiscount = "30% Off"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_collapsing_demo7)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        binding = ActivityCollapsingDemo7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.expandedTitle.text = productTitle
        binding.expandedPrice.text = productPrice
        binding.expandedOriginalPrice.text = productOriginalPrice
        binding.discountBadge.text = productDiscount

        // Set initial values for the collapsed content
        binding.collapsedTitle.text = productTitle
        binding.collapsedPrice.text = productPrice
       // binding.collapsedDiscount.text = productDiscount

            // Load the product image into the expanded view

        loadProductImage()

        // Set up the AppBarLayout offset change listener
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            val percentage = abs(verticalOffset).toFloat() / scrollRange.toFloat()

            // Toggle between expanded and collapsed layouts
            if (percentage > 0.8f && !isToolbarCollapsed) {
                isToolbarCollapsed = true
                showCollapsedContent()  // Show collapsed content with animation
            } else if (percentage < 0.8f && isToolbarCollapsed) {
                isToolbarCollapsed = false
                showExpandedContent()  // Show expanded content with animation
            }

            // Fade expanded content based on scroll percentage
            binding.expandedContent.alpha = 1f - percentage

            // Handle toolbar visibility and alpha
            handleToolbarState(percentage)
        })

        // Set up the toolbar navigation click listener
        binding.toolbar.setNavigationOnClickListener {

            this.finish()
        }


    }


    private fun showCollapsedContent() {
        binding.collapsedContent.visibility = View.VISIBLE
        binding.collapsedContent.alpha = 0f
        binding.collapsedContent.animate()
            .alpha(1f)
            .setDuration(300)  // Duration of the animation
            .setInterpolator(AccelerateDecelerateInterpolator())  // Smooth animation
            .start()

        binding.expandedContent.animate()
            .alpha(0f)
            .setDuration(300)  // Duration of the animation
            .setInterpolator(AccelerateDecelerateInterpolator())  // Smooth animation
            .withEndAction {
                binding.expandedContent.visibility = View.GONE  // Hide after animation
                // Hide individual views in expanded content
                binding.productImage.visibility = View.GONE
                binding.expandedTitle.visibility = View.GONE
                binding.expandedPrice.visibility = View.GONE
                binding.expandedOriginalPrice.visibility = View.GONE
                binding.discountBadge.visibility = View.GONE
            }
            .start()

        // Set the product thumbnail image when collapsed
        binding.productThumb.setImageDrawable(binding.productImage.drawable)
    }

    private fun showExpandedContent() {
        binding.expandedContent.visibility = View.VISIBLE
        binding.expandedContent.alpha = 0f
        binding.expandedContent.animate()
            .alpha(1f)
            .setDuration(300)  // Duration of the animation
            .setInterpolator(AccelerateDecelerateInterpolator())  // Smooth animation
            .start()

        binding.collapsedContent.animate()
            .alpha(0f)
            .setDuration(300)  // Duration of the animation
            .setInterpolator(AccelerateDecelerateInterpolator())  // Smooth animation
            .withEndAction {
                binding.collapsedContent.visibility = View.GONE  // Hide after animation
                // Show individual views in expanded content
                binding.productImage.visibility = View.VISIBLE
                binding.expandedTitle.visibility = View.VISIBLE
                binding.expandedPrice.visibility = View.VISIBLE
                binding.expandedOriginalPrice.visibility = View.VISIBLE
                binding.discountBadge.visibility = View.VISIBLE
            }
            .start()
    }


    private fun handleToolbarState(percentage: Float) {
        binding.apply {
            when {
                percentage > 0.8f -> {  // Collapsed state
                    toolbar.isVisible = true
                    toolbar.alpha = 1f
                }
                percentage < 0.2f -> {  // Expanded state
                    toolbar.isVisible = false
                }
                else -> {  // Transition state
                    toolbar.isVisible = true
                    toolbar.alpha = 1f
                }
            }
        }
    }

    private fun loadProductImage() {
        binding.productImage.load(R.drawable.ic_food) {
            crossfade(true)
            crossfade(300)
            transformations(
                RoundedCornersTransformation(
                    resources.getDimensionPixelSize(R.dimen._10dp).toFloat()
                )
            )
            listener(
                onSuccess = { _, _ ->
                    if (isToolbarCollapsed) {
                        // Set the product thumbnail image when the image is loaded
                        binding.productThumb.setImageDrawable(binding.productImage.drawable)
                    }
                }
            )
        }
    }



}