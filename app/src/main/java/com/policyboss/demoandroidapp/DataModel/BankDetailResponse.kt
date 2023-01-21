package com.policyboss.demoandroidapp.DataModel

data class BankDetailResponse(
    val data: List<BankEntity>,
    val errors: String,
    val response: String,
    val responseCode: String
)