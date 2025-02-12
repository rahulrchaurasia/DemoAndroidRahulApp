package com.policyboss.demoandroidapp.Repository

import android.content.Context
import com.policyboss.demoandroidapp.LoginModule.API.APIService

import com.squareup.okhttp.internal.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class LoginRepository (private val apiService : APIService) {


    suspend fun getLogin(strSerach: String)  = flow {



        val response = apiService.getBankDetail(strSerach)

        // endregion

        emit(response)

    }.flowOn(Dispatchers.IO)



    fun generateNumberUsingFlow() : Flow<Int> = flow {

        for(i in 1..10){

            emit(i)
            delay(2000)
        }
    }

}