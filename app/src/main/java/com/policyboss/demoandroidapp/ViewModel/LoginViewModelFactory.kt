package com.policyboss.demoandroidapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.policyboss.demoandroidapp.Repository.LoginRepository


class LoginViewModelFactory(private val repository: LoginRepository) :ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){

            return LoginViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel class Not Found")

    }
}