package com.policyboss.demoandroidapp.CoroutineDemo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.policyboss.demoandroidapp.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineViewModel : ViewModel(){

    private var Count :Int

   init {

      Count = 0
   }

   fun setCount(mCount : Int)  {
        Count = mCount
    }

    fun getCount() : Int {
       // Count = mCount
        Count = Count + 1
       return Count
    }

    fun getData() {

       viewModelScope.launch {

           while (true){
               Log.d(Constant.TAG_Coroutine, "Continue Start ViewModelScope :")
               delay(300)
           }
       }
   }
}