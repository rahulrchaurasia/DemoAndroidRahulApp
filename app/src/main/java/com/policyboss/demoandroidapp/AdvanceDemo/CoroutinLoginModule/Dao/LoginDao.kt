package com.policyboss.demoandroidapp.MVVMDemo.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.LoginEntity

@Dao
interface LoginDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLoginData(loginEntity : LoginEntity)


//    @Query( "select * from Quote")
//    suspend fun getQuote(): List<QuoteEntity>
}