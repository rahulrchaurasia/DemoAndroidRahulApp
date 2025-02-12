package com.policyboss.demoandroidapp.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DataModel.BankModel.BankDetailResponse
import com.policyboss.demoandroidapp.Repository.LoginRepository
import dagger.hilt.android.AndroidEntryPoint


import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class LoginViewModel (var loginRepository: LoginRepository) : ViewModel(){

    private val loginMutableStateFlow  :  MutableStateFlow<APIState<BankDetailResponse>> =  MutableStateFlow<APIState<BankDetailResponse>>(APIState.Empty())

    val LoginStateFlow : StateFlow<APIState<BankDetailResponse>>
    get() = loginMutableStateFlow


    private val generateNoMutableFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    val generateNoFlow: StateFlow<Int>
        get() = generateNoMutableFlow


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

    //note :launchIn creates a new coroutine for each Flow. This means both Flows run independently and concurrently.
    // That's why you see the numbers being emitted in parallel.

    // this call parallel ie "First Number star than second Number start  concurrently

    fun generateNumberUsingFlow1() = viewModelScope.launch {

        loginRepository.generateNumberUsingFlow().onEach{

            Log.d(Constant.TAG , "First Number start $it")


            generateNoMutableFlow.value = it
        }.launchIn(this)

        loginRepository.generateNumberUsingFlow().onEach{

            Log.d(Constant.TAG , "Second Number start $it")


            generateNoMutableFlow.value = it
        }.launchIn(this)
    }
//note :  When you use collect, it's a suspending function that blocks until the Flow completes.
// That's why the second Flow only starts after the first one finishes. It's sequential execution.
    // this call sequntiall ie after completion of whole "First Number star than second Number start
    fun generateNumberUsingFlow() = viewModelScope.launch {

        loginRepository.generateNumberUsingFlow().collect{

            Log.d(Constant.TAG , "First Number start $it")


            generateNoMutableFlow.value = it
        }

        loginRepository.generateNumberUsingFlow().collect{

            Log.d(Constant.TAG , "Second Number start $it")


            generateNoMutableFlow.value = it
        }
    }




}