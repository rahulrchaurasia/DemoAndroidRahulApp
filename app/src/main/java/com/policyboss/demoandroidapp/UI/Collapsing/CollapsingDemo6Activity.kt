package com.policyboss.demoandroidapp.UI.Collapsing

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.appbar.AppBarLayout
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo5Binding
import com.policyboss.demoandroidapp.databinding.ActivityCollapsingDemo6Binding
import kotlin.math.abs

class CollapsingDemo6Activity : BaseActivity() {


    lateinit var binding: ActivityCollapsingDemo6Binding
    private var isToolbarCollapsed = false
    private var currentScrollPercentage = 0f


    companion object {
        private const val ANIMATION_DURATION = 200L
        private const val COLLAPSE_THRESHOLD = 0.85f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityCollapsingDemo6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSystemBars()
        setupToolbar()
        setupContent()
    }

    private fun setupSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Handle status bar
            binding.appBarLayout.updatePadding(top = systemBars.top)

            // Handle navigation bar
            binding.nestedScrollView.updatePadding(bottom = systemBars.bottom)

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setDisplayShowTitleEnabled(false)
            }

            // Set up the collapsing toolbar listener
            appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percentage = abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
                handleToolbarTransition(percentage)
            }

            // Set initial states
            collapsedContent.alpha = 0f
            expandedContent.alpha = 1f
        }
    }

    private fun setupContent() {
        binding.apply {
            val productTitle = "Mercedes-Benz Cars"

            // Set expanded content
            expandedTitle.text = productTitle
            collapsedTitle.text = productTitle

            // Load images
            loadProductImage()

            // Setup prices
            setupPriceDetails()

            // Setup click listeners
            setupClickListeners()
        }
    }


    private fun loadProductImage() {
        binding.productImage.load(R.drawable.ic_food) {
            crossfade(true)
            crossfade(300)
            transformations(RoundedCornersTransformation(
                resources.getDimensionPixelSize(R.dimen._10dp).toFloat()
            ))
            listener(
                onSuccess = { _, _ ->
                    if (isToolbarCollapsed) {
                        binding.productThumb.setImageDrawable(binding.productImage.drawable)
                    }
                }
            )
        }
    }

    private fun setupPriceDetails() {
        binding.apply {
            val price = "₹20"
            val originalPrice = "₹300"
            val discount = "40% OFF"

            // Expanded view prices
            expandedPrice.text = price
            expandedOriginalPrice.apply {
                text = originalPrice
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            discountBadge.text = discount

            // Collapsed view prices
            collapsedPrice.text = price
            collapsedDiscount.text = discount
        }
    }

    private fun handleToolbarTransition(percentage: Float) {
        binding.apply {
            if (percentage > COLLAPSE_THRESHOLD) {
                if (collapsedContent.visibility != View.VISIBLE) {
                    collapsedContent.visibility = View.VISIBLE
                    collapsedContent.animate()
                        .alpha(1f)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                }
            } else {
                if (collapsedContent.visibility == View.VISIBLE) {
                    collapsedContent.animate()
                        .alpha(0f)
                        .setDuration(ANIMATION_DURATION)
                        .withEndAction {
                            collapsedContent.visibility = View.GONE
                        }
                        .start()
                }
            }

            // Fade expanded content
            expandedContent.alpha = (1 - percentage * 1.5f).coerceIn(0f, 1f)

            // Update toolbar background alpha
            toolbar.setBackgroundColor(adjustAlpha(
                getColor(R.color.colorPrimary),
                percentage.coerceIn(0f, 1f)
            ))
        }
    }
    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).toInt().coerceIn(0, 255)
        return Color.argb(
            alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    private fun setupClickListeners() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }


        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
