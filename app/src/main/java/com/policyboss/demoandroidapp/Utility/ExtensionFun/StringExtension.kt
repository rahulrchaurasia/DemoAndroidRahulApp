package com.policyboss.demoandroidapp.Utility.ExtensionFun

import com.policyboss.demoandroidapp.Utility.DateValidator

fun String.isValidDate(format: String = "dd-MM-yyyy") = DateValidator(format).isValid(this)