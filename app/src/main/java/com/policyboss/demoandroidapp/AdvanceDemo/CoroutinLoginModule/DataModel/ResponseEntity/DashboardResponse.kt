package com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity

import com.policyboss.demoandroidapp.LoginModule.DataModel.APIResponse
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardEntity


data class DashboardResponse(
    val MasterData: DashboardMasterData,

    ): APIResponse()


data class DashboardMasterData(
    val Dashboard: MutableList<DashboardEntity>,
    val Menu: MutableList<Any>
)