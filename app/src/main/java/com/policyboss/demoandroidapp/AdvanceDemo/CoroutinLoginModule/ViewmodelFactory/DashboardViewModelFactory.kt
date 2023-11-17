package com.policyboss.demoandroidapp.LoginModule.ViewmodelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.policyboss.demoandroidapp.LoginModule.Repository.DashboardRepository
import com.policyboss.demoandroidapp.LoginModule.ViewModel.DashboardViewModel
import com.policyboss.demoandroidapp.LoginModule.ViewModel.LoginViewModel

class DashboardViewModelFactory(private val repository: DashboardRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(DashboardViewModel::class.java)){

            return DashboardViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel class Not Found")

    }
}