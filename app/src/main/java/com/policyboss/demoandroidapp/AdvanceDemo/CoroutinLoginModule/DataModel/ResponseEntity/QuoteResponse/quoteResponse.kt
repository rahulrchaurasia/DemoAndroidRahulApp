
package com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity.QuoteResponse.QuoteEntity


data class  quoteResponse(

    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<QuoteEntity>,
    val totalCount: Int,
    val totalPages: Int
)