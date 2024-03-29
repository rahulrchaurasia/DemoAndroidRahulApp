package com.policyboss.demoandroidapp.ShareViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelDemo2 : ViewModel() {



   private val _data   = MutableLiveData<String>()

    // Required LiveData is only public therefore data ,  get() = _data means set mutabledata to it
    val data : LiveData<String>
     get() = _data

    fun setData(newData : String){

        _data.value = newData

    }

}