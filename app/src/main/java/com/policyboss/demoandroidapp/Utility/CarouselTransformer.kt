package com.demo.kotlindemoapp.HomeMain.CarouselViewPager

import android.content.Context
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.policyboss.demoandroidapp.R
import kotlin.math.abs


class CarouselTransformer(val context: Context) : ViewPager2.PageTransformer
{

    val pageMarginPx = context.resources.getDimensionPixelOffset(R.dimen._3dp)
    val offsetPx = context.resources.getDimensionPixelOffset(R.dimen._40dp)


    override fun transformPage(page: View, position: Float) {
        // Calculate the scaling based on the position

        page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
        val offset = position * -(2 * offsetPx + pageMarginPx)
        page.translationX = offset
    }
}