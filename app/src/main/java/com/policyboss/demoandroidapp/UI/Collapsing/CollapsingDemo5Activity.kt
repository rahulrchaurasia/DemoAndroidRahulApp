package com.policyboss.demoandroidapp.UI.Collapsing

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.appbar.AppBarLayout
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo4Binding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo5Binding
import kotlin.math.abs

class CollapsingDemo5Activity : BaseActivity(){

    lateinit var binding: ActivityCollapsingDemo5Binding
    // State tracking
    private var isToolbarCollapsed = false
    private var currentScrollPercentage = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)


        binding = ActivityCollapsingDemo5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBars()
        setupCollapsingToolbar()
        setupProductDetails()
        setupClickListeners()
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


//        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val scrollRange = appBarLayout.totalScrollRange
//            val percentage = abs(verticalOffset).toFloat() / scrollRange.toFloat()
//
//            handleToolbarState(percentage)
//        })

    }

    private fun setupSystemBars1() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.coordinatorLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply padding to handle insets
            binding.appBarLayout.updatePadding(
                top = insets.top
            )

            // Adjust content padding
            binding.toolbar.updatePadding(
                top = 0
            )

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupSystemBars() {
        // Handle edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Update padding for system bars
            binding.appBarLayout.updatePadding(top = systemBars.top)

            // Ensure toolbar doesn't get extra padding
            binding.toolbar.updatePadding(top = 0)

            // Handle navigation bar
            binding.nestedScrollView.updatePadding(bottom = systemBars.bottom)

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupCollapsingToolbar() {
        binding.apply {
            // Hide default title during transition
            collapsingToolbar.title = ""

            // Configure toolbar
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false) // Hide default title
            }

            // Configure collapsing behavior
            appBarLayout.addOnOffsetChangedListener(
                AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    val scrollRange = appBarLayout.totalScrollRange
                    currentScrollPercentage = abs(verticalOffset).toFloat() / scrollRange.toFloat()

                    handleToolbarTransition(currentScrollPercentage)
                }
            )

            // Initial states
            collapsedContent.alpha = 0f
            collapsedContent.visibility = View.GONE
            expandedContent.alpha = 1f
        }
    }




    private fun setupProductDetails1() {
        binding.apply {
            // Set expanded content
            expandedTitle.text = getString(R.string.product_title)
            expandedPrice.text = getString(R.string.price_format, 20)
            expandedOriginalPrice.text = getString(R.string.price_format, 300)
            discountPercentage.text = getString(R.string.discount_format, 40)

            // Set collapsed content
            collapsedTitle.text = getString(R.string.product_title)
            collapsedPrice.text = getString(R.string.price_format, 20)

            // Load product image
            loadProductImage()
        }
    }

    private fun setupProductDetails() {
        binding.apply {
            // Product details
            val productTitle = "Mercedes-Benz Cars"
            val currentPrice = "₹20"
            val originalPrice = "₹300"
            val discount = "40% OFF"

            // Set expanded content
            expandedTitle.text = productTitle
            expandedPrice.text = currentPrice
            expandedOriginalPrice.apply {
                text = originalPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            discountPercentage.text = discount

            // Set collapsed content
            collapsedTitle.text = productTitle
            collapsedPrice.text = currentPrice

            // Load images
            loadProductImage()
        }
    }

    private fun handleToolbarTransition(percentage: Float) {
        val collapseThreshold = 0.85f

        binding.apply {
            // Handle collapse state changes
            if (percentage > collapseThreshold && !isToolbarCollapsed) {
                isToolbarCollapsed = true
                animateToCollapsed()
            } else if (percentage < collapseThreshold && isToolbarCollapsed) {
                isToolbarCollapsed = false
                animateToExpanded()
            }

            // Continuous animations
            val fadeOutFactor = 1.5f
            expandedContent.alpha = (1 - (percentage * fadeOutFactor)).coerceIn(0f, 1f)
            expandedContent.translationY = -(percentage * 50f) // Parallax effect

            // Handle toolbar background alpha
            toolbar.setBackgroundColor(adjustAlpha(
                getColor(R.color.colorPrimary),
                (percentage * 1.5f).coerceIn(0f, 1f)
            ))
        }
    }

    private fun animateToCollapsed() {
        binding.apply {
            collapsedContent.visibility = View.VISIBLE
            collapsedContent.animate()
                .alpha(1f)
                .setDuration(200)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withStartAction {
                    // Update thumbnail
                    productThumb.setImageDrawable(productImage.drawable)
                }
                .start()
        }
    }

    private fun animateToExpanded() {
        binding.apply {
            collapsedContent.animate()
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(FastOutLinearInInterpolator())
                .withEndAction {
                    collapsedContent.visibility = View.GONE
                }
                .start()
        }
    }
    private fun animateToolbarTransition(collapse: Boolean) {
        binding.apply {
            // Animate collapsed content
            collapsedContent.animate()
                .alpha(if (collapse) 1f else 0f)
                .setDuration(200)
                .withStartAction {
                    if (collapse) {
                        collapsedContent.visibility = View.VISIBLE
                        // Update thumbnail when collapsing
                        productThumb.setImageDrawable(productImage.drawable)
                    }
                }
                .withEndAction {
                    if (!collapse) {
                        collapsedContent.visibility = View.GONE
                    }
                }
                .start()
        }
    }





    private fun loadProductImage() {
        binding.apply {
            productImage.load(R.drawable.ic_food) {
                crossfade(true)
                crossfade(300)
                transformations(
                    RoundedCornersTransformation(
                        resources.getDimensionPixelSize(R.dimen._10dp).toFloat()
                    )
                )
//                placeholder(R.drawable.ic_placeholder)
//                error(R.drawable.ic_error)
                listener(
                    onSuccess = { _, _ ->
                        // Copy to thumbnail if already collapsed
                        if (isToolbarCollapsed) {
                            productThumb.setImageDrawable(productImage.drawable)
                        }
                    }
                )
            }
        }
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).toInt().coerceIn(0, 255)
        return Color.argb(alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }


    private fun  setupClickListeners(){


    }

}