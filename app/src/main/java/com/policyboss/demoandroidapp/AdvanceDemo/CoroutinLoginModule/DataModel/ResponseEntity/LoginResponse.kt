package com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity

import com.policyboss.demoandroidapp.LoginModule.DataModel.APIResponse
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.LoginEntity

data class LoginResponse (
    val MasterData: LoginEntity

): APIResponse()