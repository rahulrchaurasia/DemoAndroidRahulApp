package com.policyboss.demoandroidapp.AdvanceDemo.Room.Convertors

import androidx.room.TypeConverter
import androidx.room.TypeConverters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.policyboss.demoandroidapp.MVVMDemo.Data.DashboardData.DashboardList
import java.util.*

class Convertors {


    @TypeConverter
    fun fromDateToLong(value : Date) : Long{

        return value.time
    }

    @TypeConverter
    fun fromLongToDate(value: Long) : Date {

        return Date(value)
    }



   // region we can use this also for saving Nested List
//    @TypeConverter
//    fun fromDashboardrList(value: List<DashboardList>): String {
//        val gson = Gson()
//        val type = object : TypeToken<List<DashboardList>>() {}.type
//        return gson.toJson(value, type)
//    }
//
//    @TypeConverter
//    fun toDashboardList(value: String): List<DashboardList> {
//        val gson = Gson()
//        val type = object : TypeToken<List<DashboardList>>() {}.type
//        return gson.fromJson(value, type)
//    }

    //endregion


    @TypeConverter
    fun saveDashboardrList(listOfString: List<DashboardList?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getDashboardrList(listOfString: String?): List<DashboardList?>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<String?>?>() {}.type
        )
    }
}