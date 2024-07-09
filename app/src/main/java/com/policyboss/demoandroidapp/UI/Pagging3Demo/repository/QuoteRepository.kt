package com.policyboss.demoandroidapp.UI.Pagging3Demo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.policyboss.demoandroidapp.AdvanceDemo.API.QuoteAPI
import com.policyboss.demoandroidapp.UI.Pagging3Demo.pagging.QuotePagingSource
import javax.inject.Inject

class QuoteRepository @Inject constructor(private val quoteAPI: QuoteAPI) {

    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, maxSize = 100),
        pagingSourceFactory = { QuotePagingSource(quoteAPI) }
    ).flow
}