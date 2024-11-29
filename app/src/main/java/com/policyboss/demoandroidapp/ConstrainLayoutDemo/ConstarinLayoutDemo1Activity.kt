package com.policyboss.demoandroidapp.ConstrainLayoutDemo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.policyboss.demoandroidapp.BaseActivity
import com.policyboss.demoandroidapp.R
import com.policyboss.demoandroidapp.databinding.ActivityConstarinLayoutDemo1Binding
import com.policyboss.demoandroidapp.databinding.ActivityScrollViewWithTabBinding


/*
Note :
layout_constraintHorizontal_weight in chains:

Always maintains the proportional ratios (40:35:25)
Respects the available space after considering margins/padding
Adjusts dynamically if space is constrained
Maintains relationships between elements even when space is tight

layout_constraintWidth_percent:

Takes percentage of the parent's total width
Doesn't account for margins/padding in its calculations
Can cause overlap or layout issues when space is tight
Might break layout constraints when content doesn't fit
 */

/*Note



let me break down the ideal scenarios for both approaches:
layout_constraintWidth_percent is best when:
1. You need exact, fixed percentages regardless of siblings
* Example: A banner that must always be exactly 75% of screen width
* Full-width cards where one section needs to be exactly 30%
* Image galleries with precise size requirements
* Header layouts where elements need specific proportions of total width
xml

<ImageView
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    app:layout_constraintWidth_percent="0.75"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintEnd_toEndOf="parent"/>

 
layout_constraintHorizontal_weight (Chains) is best when:
1. Elements need to maintain proportions relative to each other
* Navigation buttons in a row
* Equal-width elements (like bottom navigation)
* Form layouts where fields should divide space
* Responsive layouts that need to adapt to different screen sizes

Simple rule of thumb:
* Use width_percent when an element needs a fixed percentage of the parent
* Use chains when elements need to share space proportionally with siblings

 */
class ConstarinLayoutDemo1Activity : BaseActivity() {

    private lateinit var binding: ActivityConstarinLayoutDemo1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConstarinLayoutDemo1Binding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {

            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = "3 Button with Equal Width"


        }

    }
}