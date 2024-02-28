package com.policyboss.demoandroidapp.ViewPagerDemo.model

import android.icu.text.CaseMap.Title

data class FoodEntity (
    val id: Int,
    val title: String,
    val imageUrl: String,
    var progress: Float = 0f,
    var progressHeader :String = "",
    var isUpdate :Boolean = false
)