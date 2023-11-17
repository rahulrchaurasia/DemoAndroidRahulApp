package com.policyboss.demoandroidapp.AdvanceDemo.API


import ConstantDataResponse
import com.policyboss.demoandroidapp.AdvanceDemo.RetrofitHelper1
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface DemoAPI {


//    @Headers("token:"+ RetrofitHelper.token)
//    @GET("/quote/Postfm/user-constant-pb")
//    suspend fun getConstant(@Body body: HashMap<String, String>) : Call<quoteResponse>

    /***********  Note  **************
     Call<quoteResponse>   is Retrofit method  insteda we have to use

    Response<quoteResponse> Coroutine Method for getting the result.
    ************************/

    @FormUrlEncoded
    @Headers("token:"+ RetrofitHelper1.token)
    @POST("/quote/Postfm/user-constant-pb")
    suspend fun getConstant1(
        @Field("fbaid") fbaID: String,
        @Field("appTypeId") appTypeId: String
    ) : Response<ConstantDataResponse>



    @Headers("token:"+ RetrofitHelper1.token)
    @POST("/quote/Postfm/user-constant-pb")
    suspend fun getConstant(@Body body: HashMap<String, String>) : Response<ConstantDataResponse>


}