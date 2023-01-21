package com.policyboss.demoandroidapp

import com.example.jetpackdemo.LoginModule.API.APIService

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    val BASE_URL = "http://transactionapi.tech-sevenpay.com"

    const val token = "1234567890"
    internal var restAdapter: Retrofit? = null




    private val retrofitInstance by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }



    val retrofitLoginApi by lazy {
        retrofitInstance.create(APIService::class.java)

    }


}