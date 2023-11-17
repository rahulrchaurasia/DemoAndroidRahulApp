package com.policyboss.demoandroidapp.LoginModule.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity.LoginResponse
import com.policyboss.demoandroidapp.LoginModule.Repository.LoginRepository

import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.APIState1
import com.policyboss.demoandroidapp.LoginModule.DataModel.RequestEntity.LoginRequestEntity

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel (var loginRepository: LoginRepository) : ViewModel(){


    private val loginMutableStateFlow  :  MutableStateFlow<APIState1<LoginResponse>> =  MutableStateFlow<APIState1<LoginResponse>>(APIState1.Empty())

    val LoginStateFlow : StateFlow<APIState1<LoginResponse>>
    get() = loginMutableStateFlow



    private val loginMutableLiveData =  MutableLiveData<APIState1<LoginResponse>>()

    val loginLiveData : LiveData<APIState1<LoginResponse>>
    get() = loginMutableLiveData




    fun getLoginUsingFlow(loginRequestEntity: LoginRequestEntity) = viewModelScope.launch {

        loginMutableStateFlow.value = APIState1.Loading()

        try {


            loginRepository.getLogin(loginRequestEntity)

                .catch { ex ->

                                 // emit(APIState1.Failure(ex.message ?: "Unknown Error"))

                    loginMutableStateFlow.value = APIState1.Failure(ex.message ?: "Unknown Error")

                    //loginMutableStateFlow.emit(APIState1.Failure(ex.message ?: "Unknown Error"))

                }.collect{ data ->

                    if(data.isSuccessful){
                        if(data.body()?.StatusNo == 0){


                            loginMutableStateFlow.value = APIState1.Success(data =  data.body()!!)
                           // loginMutableStateFlow.emit(APIState1.Success(data =  data.body()!!))
                        }else{

                            loginMutableStateFlow.value = APIState1.Failure(data.body()?.Message ?: Constant.ErrorMessage)
                          //  loginMutableStateFlow.emit(APIState1.Failure(data.body()?.Message ?: Constant.ErrorMessage))
                        }
                    }else{
                        loginMutableStateFlow.value = APIState1.Failure(data.message() ?: Constant.ErrorMessage)
                       // loginMutableStateFlow.emit(APIState1.Failure(data.body()?.Message ?: Constant.ErrorMessage))
                    }




                }

        }catch (ex : Exception){

            loginMutableStateFlow.value = APIState1.Failure(ex.message ?: Constant.ErrorDefault)
            //loginMutableStateFlow.emit(APIState1.Failure(ex.message ?: Constant.ErrorDefault))

        }

    }



    fun getLoginUsingLiveData(loginRequestEntity: LoginRequestEntity) = viewModelScope.launch {




//        loginRepository.getLogin2(loginRequestEntity).collect{
//
//           loginMutableLiveData.postValue(it)
//
//
//        }


    }

}