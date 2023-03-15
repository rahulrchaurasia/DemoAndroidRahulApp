package com.policyboss.demoandroidapp.DataModel.BankModel

data class BankDetailResponse(
    val data: List<BankEntity>,
    val errors: String,
    val response: String,
    val responseCode: String
)