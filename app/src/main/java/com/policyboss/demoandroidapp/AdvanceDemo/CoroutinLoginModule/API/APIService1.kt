package com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.API

import ConstantDataResponse
import com.policyboss.demoandroidapp.LoginModule.DataModel.RequestEntity.LoginRequestEntity
import com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity.DashboardResponse
import com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity.LoginResponse
import com.policyboss.demoandroidapp.AdvanceDemo.RetrofitHelper1


import retrofit2.Response
import retrofit2.http.*

interface APIService1 {


//    @Headers("token:"+ RetrofitHelper1.token)
//    @GET("/quote/Postfm/user-constant-pb")
//    suspend fun getConstant(@Body body: HashMap<String, String>) : Call<quoteResponse>

    /***********  Note  **************
     Call<quoteResponse>   is Retrofit method  insteda we have to use

    Response<quoteResponse> Coroutine Method for getting the result.
    ************************/




    @Headers("token:"+ RetrofitHelper1.token)
    @POST("/quote/Postfm/login")
    suspend fun getLogin(
        @Body body: LoginRequestEntity
    ) : Response<LoginResponse>


    @Headers("token:"+ RetrofitHelper1.token)
    @POST("/quote/Postfm/user-constant-pb")
    suspend fun getConstant(@Body body: HashMap<String, String>) : Response<ConstantDataResponse>


    @Headers("token:"+ RetrofitHelper1.token)
    @POST("/quote/Postfm/get-dynamic-app-pb")
    suspend fun getDynamicDashBoard(@Body body: HashMap<String,String>) : Response<DashboardResponse>


}