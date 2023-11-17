package com.policyboss.demoandroidapp.MVVMDemo.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.policyboss.demoandroidapp.MVVMDemo.Data.DashboardData.ConstantEntity

@Dao
interface ConstantDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun createConstant(quote : ConstantEntity)


//    @Query( "select * from Quote")
//    suspend fun getQuote(): List<QuoteEntity>
}