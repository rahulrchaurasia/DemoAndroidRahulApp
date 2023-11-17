package com.policyboss.demoandroidapp.LoginModule.Repository

import ConstantDataResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.API.APIService1
import com.policyboss.demoandroidapp.AdvanceDemo.Room.Database.DemoDatabase
import com.policyboss.demoandroidapp.Constant

import com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity.DashboardResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field


//https://medium.com/android-beginners/mvvm-with-kotlin-coroutines-and-retrofit-example-d3f5f3b09050

class DashboardRepository(
    private val  apiService : APIService1,
    private val demoDatabase: DemoDatabase
){


//    private val userConstantMutableLiveData = MutableLiveData<ConstantDataResponse>()
//
//    val userConstant : LiveData<ConstantDataResponse>
//    get() =  userConstantMutableLiveData




    suspend fun getUserConstandDataFromAPI(@Body body: HashMap<String, String>) : Response<ConstantDataResponse> =   apiService.getConstant(body)





    suspend fun getUserConstandDataUsingWithFromAPI(@Body body: HashMap<String, String>): Response<ConstantDataResponse> = withContext(Dispatchers.IO) {


        val result =  apiService.getConstant(body)


        ////////

        if(result?.body() != null){


            if(result.isSuccessful){

                result.body()?.let {

                    if(it.StatusNo == 0){
                        Log.d(Constant.TAG_Coroutine,"Data Come from service")

                        demoDatabase.constantDao().createConstant(it.MasterData)

                    }
                }

            }

        }


        return@withContext result

    }


   // No need to set again Dispatchers.IO using withContext because in ViewModel it is already set in  Dispatchers.IO
    suspend fun getDynamicDashBoard(@Body body: HashMap<String,String>) : Response<DashboardResponse> =  apiService.getDynamicDashBoard(body)


    suspend fun getDynamicDashBoard1(@Body body: HashMap<String,String>) : Response<DashboardResponse> = withContext(Dispatchers.IO){



        val  result =  apiService.getDynamicDashBoard(body)

        return@withContext result
    }

}