package com.policyboss.demoandroidapp.LoginModule.ViewModel

import ConstantDataResponse
import android.util.Log
import androidx.lifecycle.*
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.ViewModel.ResponseOLD
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity.DashboardResponse
import com.policyboss.demoandroidapp.LoginModule.Repository.DashboardRepository

import kotlinx.coroutines.*


class DashboardViewModel(private  val repository: DashboardRepository) : ViewModel() {

   private val _constantData = MutableLiveData<ResponseOLD<ConstantDataResponse>>()
    private val _dashBoardData = MutableLiveData<ResponseOLD<DashboardResponse>>()

    val constantData : LiveData<ResponseOLD<ConstantDataResponse>>
    get() = _constantData

    val dashBoardDataLiveData : LiveData<ResponseOLD<DashboardResponse>>
    get() = _dashBoardData


    //No need to Observe UseConstand Api ie constantData
    fun getParallelAPIForUser_Dasbboard(fbaID : String){

        _dashBoardData.postValue(ResponseOLD.Loading())    // Calling Loading Function

        val body = HashMap<String, String>()
        body["fbaid"] = fbaID

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val resultDashboardAsync = async {  repository.getDynamicDashBoard1(body)}

                val resultConstantAsync = async {  repository.getUserConstandDataUsingWithFromAPI(body) }


                val resultDashboard = resultDashboardAsync.await()
                val resultConstant = resultConstantAsync.await()

                //val result = repository.getDynamicDashBoard(body)
                withContext(Dispatchers.Main){
                    if(resultDashboard.isSuccessful  && resultConstant.isSuccessful){

                        // region DashBoardSuccess
                        if(resultDashboard.body()?.StatusNo == 0){
                            _dashBoardData.postValue(ResponseOLD.Success(resultDashboard.body()) )  //For Dashboard

                        }else{
                            _dashBoardData.postValue(ResponseOLD.Error(resultDashboard.body()!!.Message) )
                        }
                        //endregion



                    }else{

                        Log.d(Constant.TAG_Coroutine, resultDashboard.message())
                        _dashBoardData.postValue(ResponseOLD.Error(resultDashboard.errorBody().toString()) )




                    }
                }
            } catch (exception: Exception) {
                _dashBoardData.postValue(ResponseOLD.Error( "Error Occurred!" + exception.message) )
            }
        }
    }


    //If we wann to Observe Both constantData and dashBoardDataLiveData
    // But mostly when parallel api is called we don't required to observe all api.

    fun getParallelAPIForUser_Dasbboard_ObservingBoth(fbaID : String){

        _dashBoardData.postValue(ResponseOLD.Loading())    // Calling Loading Function

        val body = HashMap<String, String>()
        body["fbaid"] = fbaID

        viewModelScope.launch(Dispatchers.IO) {

            try {
                val resultDashboardAsync = async {  repository.getDynamicDashBoard(body)}

                val resultConstantAsync = async {  repository.getUserConstandDataUsingWithFromAPI(body) }


                val resultDashboard = resultDashboardAsync.await()
                val resultConstant = resultConstantAsync.await()

                //val result = repository.getDynamicDashBoard(body)
                withContext(Dispatchers.Main){
                    if(resultDashboard.isSuccessful  && resultConstant.isSuccessful){

                        // region DashBoard and UserConstant Success
                        // region DashBoardSuccess
                        if(resultDashboard.body()?.StatusNo == 0){
                            _dashBoardData.postValue(ResponseOLD.Success(resultDashboard.body()) )  //For Dashboard

                        }else{
                            _dashBoardData.postValue(ResponseOLD.Error(resultDashboard.body()!!.Message) )
                        }
                        //endregion

                        // region ConstantSuccess
                        if(resultConstant.body()?.StatusNo == 0){
                            _constantData.postValue(ResponseOLD.Success(resultConstant.body()) )   // For Live
                        }else{
                            _constantData.postValue(ResponseOLD.Error(resultConstant.body()!!.Message) )
                        }

                        //endregion

                        //endregion


                    }
                    else{
                        // region Handling Dashboard and Constant Failure
                        if(!resultDashboard.isSuccessful){
                            Log.d(Constant.TAG_Coroutine, resultDashboard.message())
                            _dashBoardData.postValue(ResponseOLD.Error(resultDashboard.errorBody().toString()) )

                        }
                        if(!resultConstant.isSuccessful) {
                            Log.d(Constant.TAG_Coroutine, resultConstant.message())
                            _constantData.postValue(ResponseOLD.Error(resultConstant.errorBody().toString()))
                        }
                        //endregion


                    }
                }
            } catch (exception: Exception) {
                _dashBoardData.postValue(ResponseOLD.Error( "Error Occurred!" + exception.message) )
            }
        }
    }



}