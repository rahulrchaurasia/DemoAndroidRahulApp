package com.policyboss.demoandroidapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DataModel.BankModel.BankDetailResponse
import com.policyboss.demoandroidapp.Repository.LoginRepository


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel (var loginRepository: LoginRepository) : ViewModel(){


    private val loginMutableStateFlow  :  MutableStateFlow<APIState<BankDetailResponse>> =  MutableStateFlow<APIState<BankDetailResponse>>(APIState.Empty())

    val LoginStateFlow : StateFlow<APIState<BankDetailResponse>>
    get() = loginMutableStateFlow



//    private val loginMutableLiveData =  MutableLiveData<APIState<BankDetailResponse>>()
//
//    val loginLiveData : LiveData<APIState<LoginResponse>>
//    get() = loginMutableLiveData




    fun getLoginUsingFlow(strSerach: String) = viewModelScope.launch {

        loginMutableStateFlow.value = APIState.Loading()

        try {


            loginRepository.getLogin(strSerach)

                .catch { ex ->

                                 // emit(APIState.Failure(ex.message ?: "Unknown Error"))

                    loginMutableStateFlow.value = APIState.Failure(ex.message ?: "Unknown Error")

                    //loginMutableStateFlow.emit(APIState.Failure(ex.message ?: "Unknown Error"))

                }.collect{ data ->

                    if(data.isSuccessful){
                        if(data.body()?.response!!.equals("Success")){


                            loginMutableStateFlow.value = APIState.Success(data =  data.body()!!)
                           // loginMutableStateFlow.emit(APIState.Success(data =  data.body()!!))
                        }else{

                            loginMutableStateFlow.value = APIState.Failure(data.body()?.errors ?: Constant.ErrorMessage)
                          //  loginMutableStateFlow.emit(APIState.Failure(data.body()?.Message ?: Constant.ErrorMessage))
                        }
                    }else{
                        loginMutableStateFlow.value = APIState.Failure(data.message() ?: Constant.ErrorMessage)
                       // loginMutableStateFlow.emit(APIState.Failure(data.body()?.Message ?: Constant.ErrorMessage))
                    }




                }

        }catch (ex : Exception){

            loginMutableStateFlow.value = APIState.Failure(ex.message ?: Constant.ErrorDefault)
            //loginMutableStateFlow.emit(APIState.Failure(ex.message ?: Constant.ErrorDefault))

        }

    }





}