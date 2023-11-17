package com.policyboss.demoandroidapp.MVVMDemo.Data.DashboardData

import androidx.room.TypeConverters
import com.google.gson.Gson

data class DashboardList(
    val ProdId: String,
    val url: String
)

//class DashboardListTypeConverter(){
//
//    @TypeConverters
//    fun listToJson(value : List<DashboardList>?) = Gson().toJson(value)
//
//    @TypeConverters
//    fun jsonToList(value : String) = Gson().fromJson(value,Array<DashboardList> :: class.java).toList()
//}