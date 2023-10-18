package com.example.jetpackdemo.LoginModule.API


import com.policyboss.demoandroidapp.DataModel.BankModel.BankDetailResponse
import com.policyboss.demoandroidapp.request.LoginRequestModel
import com.policyboss.demoandroidapp.response.Login.LoginResponse

import retrofit2.Response
import retrofit2.http.*

interface APIService {

    // http://transactionapi.tech-sevenpay.com/api/DMTTransaction/GetBanksInfo?searchString=maha

    @GET("/api/DMTTransaction/GetBanksInfo")
    suspend fun getBankDetail( @Query("searchString") search : String) : Response<BankDetailResponse>

    @POST("Account/GetToken")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): Response<LoginResponse>

}