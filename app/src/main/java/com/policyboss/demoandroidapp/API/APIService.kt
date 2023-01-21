package com.example.jetpackdemo.LoginModule.API


import com.policyboss.demoandroidapp.DataModel.BankDetailResponse

import retrofit2.Response
import retrofit2.http.*

interface APIService {

    // http://transactionapi.tech-sevenpay.com/api/DMTTransaction/GetBanksInfo?searchString=maha

    @GET("/api/DMTTransaction/GetBanksInfo")
    suspend fun getBankDetail( @Query("searchString") search : String) : Response<BankDetailResponse>

}