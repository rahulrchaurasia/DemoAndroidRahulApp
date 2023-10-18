package com.policyboss.demoandroidapp.di

import com.policyboss.demoandroidapp.HiltDemo.PrefManager
import com.policyboss.demoandroidapp.facade.SevenPayPrefsManager

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class TokenInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: SevenPayPrefsManager

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request().newBuilder()
        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }

}