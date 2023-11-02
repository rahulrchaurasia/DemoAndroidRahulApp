package com.policyboss.demoandroidapp.AdvanceDemo

import com.policyboss.demoandroidapp.LoginModule.API.APIService

import com.google.gson.GsonBuilder
import com.policyboss.demoandroidapp.AdvanceDemo.API.QuoteAPI
import com.policyboss.demoandroidapp.AdvanceDemo.CoroutinLoginModule.API.APIService1
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper1 {

    val BASE_URL = "https://horizon.policyboss.com:5443"
    val BASE_URL_Quote = "https://quotable.io"
    const val token = "1234567890"
    internal var restAdapter: Retrofit? = null


    //region Commented
//    fun getInstance() : Retrofit {
//
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//    }

    //endregion


    fun getInstance() : Retrofit {
        if (restAdapter == null) {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val gson = GsonBuilder()
                .serializeNulls()
                // .setLenient()
                .create()

            val okHttpClient = okhttp3.OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build()

            restAdapter = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        }
        return restAdapter as Retrofit
    }


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

    private val retrofitQuote by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL_Quote)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val retrofitLoginApi by lazy {
        retrofitInstance.create(APIService1::class.java)

    }

    val retrofitQuoteApi by lazy {
        retrofitQuote.create(QuoteAPI::class.java)
    }
}