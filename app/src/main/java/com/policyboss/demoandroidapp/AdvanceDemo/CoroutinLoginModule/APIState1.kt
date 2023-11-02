package com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule


sealed class APIState1<T> (val data: T? = null,
                           val errorMessage: String? = null){
    class Loading<T> : APIState1<T>()
    class Success<T>(data: T? = null) : APIState1<T>(data = data)
    class Failure<T>( errorMessage: String) : APIState1<T>(errorMessage = errorMessage)
    class Empty<T>: APIState1<T>()
}

/*********************************************************************
  note : Below is not handled when we used Generic parameter so for
       that we must used Sealed class also have parameterized
 ************************************************************* */
//sealed class APIState<T> ( ){          // Not handled in Generic Type data used
//     class Loading<T> : APIState<T>()
//     class Success<T>(data: T? = null) : APIState<T>()
//     class Failure<T>( errorMessage: String) : APIState<T>()
//     class Empty<T>: APIState<T>()
//}


//sealed class APIStatePract<T> ( val data: T,
//                           val errorMessage: String? = null, val num : Int){
//    class Loading<T>(data: T) : APIStatePract<T>(data)
//    class Success<T>(data: T) : APIStatePract<T>(data = data)
//    class Failure<T>( errorMessage: String) : APIStatePract<T>(errorMessage = errorMessage)
//    class Empty<T>(data: T) : APIStatePract<T>(data)
//}

// Old  : Unable to handle Empty {Best for live Data}
//sealed class NetworkResult1<T> {
//    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
//    data class Success<T>(val data: T) : NetworkResult<T>()
//    data class Failure<T>(val errorMessage: String) : NetworkResult<T>()
//}
