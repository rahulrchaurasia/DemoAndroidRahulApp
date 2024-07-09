package com.policyboss.demoandroidapp.AdvanceDemo.API


import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.DataModel.ResponseEntity.quoteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface QuoteAPI {

   // https://quotable.io/quotes?page=1
   @GET("/quotes")
    suspend fun getQuotes( @Query("page") page : Int) : Response<quoteResponse>

//    @GET("/quotes")
//    suspend fun getQuotes(@Query("page") page: Int): QuoteList
}