package com.policyboss.demoandroidapp.LoginModule.Repository

import com.policyboss.demoandroidapp.LoginModule.DataModel.RequestEntity.LoginRequestEntity
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.API.APIService1
import com.policyboss.demoandroidapp.AdvanceDemo.Room.Database.DemoDatabase
import com.policyboss.demoandroidapp.response.Login.LoginResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

class LoginRepository (private val apiService : APIService1,
                      // private val demoDatabase: DemoDatabase
    ) {


    suspend fun getLogin(loginRequestEntity: LoginRequestEntity) = flow {



        val response = apiService.getLogin(loginRequestEntity)


        //region For Handling Database
        if(response.isSuccessful) {

            if (response.body()?.StatusNo == 0) {

                response.body()?.let {
                  //  demoDatabase.loginDao().insertLoginData(it.MasterData)
                }

            }
        }

        // endregion

        emit(response)

    }.flowOn(Dispatchers.IO)






    // suspend fun getLogin(loginRequestEntity: LoginRequestEntity) = apiService.getLogin(loginRequestEntity)

    suspend fun getLogin2(loginRequestEntity: LoginRequestEntity) = flow {

        emit(APIState.Loading() )

        val response = apiService.getLogin(loginRequestEntity)

        if(response.isSuccessful){

            if (response.body()?.StatusNo == 0) {
                emit(APIState.Success(response.body()))
            }else{
                emit(APIState.Failure(response.body()?.Message ?: Constant.ErrorDefault))

            }

        }else{
            emit(APIState.Failure(response.body()?.Message ?: Constant.ErrorDefault))

        }


    }.catch { e ->

        emit(APIState.Failure("Caught From Repository" + e.message ?: Constant.ErrorDefault))
    }.flowOn(Dispatchers.IO)



}