package com.policyboss.demoandroidapp.LoginModule.DataModel.RequestEntity

data class LoginRequestEntity(
    val AppID: String,
    val AppPASSWORD: String,
    val AppUSERID: String,
    val DeviceId: String,
    val DeviceName: String,
    val DeviceOS: String,
    val EmailId: String,
    val FBAId: Int,
    val IpAdd: String,
    val IsChildLogin: String,
    val LastLog: String,
    val MobileNo: String,
    val OldPassword: String,
    val Password: String,
    val TokenId: String,
    val UserId: Int,
    val UserName: String,
    val UserType: String,
    val VersionNo: String
)