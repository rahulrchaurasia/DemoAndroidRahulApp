package com.policyboss.demoandroidapp.UI.Pagging3Demo.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.policyboss.demoandroidapp.AdvanceDemo.API.QuoteAPI
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity.QuoteResponse.QuoteEntity
import com.policyboss.demoandroidapp.Constant

class QuotePagingSource(private val quoteAPI: QuoteAPI) : PagingSource<Int, QuoteEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, QuoteEntity> {
        return try {
            val position = params.key ?: 1
            val response = quoteAPI.getQuotes(position)

            if (response.isSuccessful) {
                response.body()?.let { body ->

                    LoadResult.Page(
                        data = body.results,
                        prevKey = if (position == 1) null else position - 1,
                        nextKey = if (position == response.body()!!.totalPages) null else position + 1
                    )
                } ?: LoadResult.Error(Exception(Constant.EmptyResponse))
            } else {
                LoadResult.Error(Exception( Constant.StatusMessage +" ${response.code()}"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, QuoteEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}