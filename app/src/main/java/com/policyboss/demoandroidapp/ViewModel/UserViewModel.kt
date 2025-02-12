package com.policyboss.demoandroidapp.ViewModel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policyboss.demoandroidapp.LoginModule.API.APIService
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.Repository.UserRepository
import com.policyboss.demoandroidapp.facade.SevenPayPrefsManager
import com.policyboss.demoandroidapp.request.LoginRequestModel
import com.policyboss.demoandroidapp.response.Login.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
 private  val sharePref : SevenPayPrefsManager,
 private  val userRepository: UserRepository
) : ViewModel(){

    private val loginMutableFlow: MutableStateFlow<APIState<LoginResponse>> =
        MutableStateFlow<APIState<LoginResponse>>(APIState.Empty())

    val loginResponse: StateFlow<APIState<LoginResponse>>
        get() = loginMutableFlow


    private val generateNoMutableFlow : MutableStateFlow<Int> = MutableStateFlow(0)
    val generateNoFlow: StateFlow<Int>
        get() = generateNoMutableFlow


    fun login(loginRequestModel: LoginRequestModel) = viewModelScope.launch {

        loginMutableFlow.value = APIState.Loading()

       userRepository.login(loginRequestModel)
           .catch {

               loginMutableFlow.value = APIState.Failure(it.message ?: "Login failed")
           }.collect{
               if (it.isSuccessful) {
                   if (it.body() != null && it.body()?.responseCode == 0) {
                       sharePref.saveUser(it.body()?.data)
                       loginMutableFlow.value = APIState.Success(it.body())
                   } else {
                       loginMutableFlow.value =
                           APIState.Failure(it.body()?.response?: Constant.ErrorMessage )
                   }
               } else {
                   loginMutableFlow.value =
                       APIState.Failure(it.message())
               }
           }
   }



    fun generateNumberUsingFlow() = viewModelScope.launch {

        userRepository.generateNumberUsingFlow().collect{

            Log.d(Constant.TAG , "Number generated"+ it.toString())


            generateNoMutableFlow.value = it
        }
    }

}