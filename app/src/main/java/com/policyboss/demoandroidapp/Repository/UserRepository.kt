package com.policyboss.demoandroidapp.Repository

import com.example.jetpackdemo.LoginModule.API.APIService
import com.policyboss.demoandroidapp.request.LoginRequestModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService : APIService
) {

    suspend fun login(loginRequestModel: LoginRequestModel) = flow {
        val response = apiService.login(loginRequestModel)
        emit(response)
    }.flowOn(Dispatchers.IO)


}