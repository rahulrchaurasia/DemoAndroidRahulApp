package com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.ViewModel

sealed class ResponseOLD <T>(val data : T? = null, val errorMessage: String? = null)
{
    class Loading<T> : ResponseOLD<T>()
    class Success<T>(data: T? = null) : ResponseOLD<T>(data = data)
    class Error<T>(errorMessage : String) : ResponseOLD<T>(errorMessage = errorMessage)

}
