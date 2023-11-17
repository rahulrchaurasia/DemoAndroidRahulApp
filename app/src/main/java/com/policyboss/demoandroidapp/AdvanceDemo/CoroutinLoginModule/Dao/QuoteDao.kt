package com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity.QuoteResponse.QuoteEntity


@Dao
interface QuoteDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun addQuote(quote : List<QuoteEntity>)


    @Query( "select * from Quote")
    suspend fun getQuote(): List<QuoteEntity>
}