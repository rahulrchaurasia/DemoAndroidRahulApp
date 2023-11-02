package com.policyboss.demoandroidapp.di

import android.content.Context
import com.policyboss.demoandroidapp.LoginModule.API.APIService
import com.policyboss.demoandroidapp.BuildConfig
import com.policyboss.demoandroidapp.facade.SevenPayPrefsManager

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    //region retrofit setup

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.CONSOLE_BASE_URL)
    }

    @Singleton
    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        tokenInterceptor: TokenInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .protocols(Collections.singletonList(okhttp3.Protocol.HTTP_1_1))
            .build()
    }

    //endregion

    //region shared Preference

    @Singleton
    @Provides
    fun providesSharedPref(@ApplicationContext appContext: Context): SevenPayPrefsManager {
        return SevenPayPrefsManager(appContext)
    }

    //endregion

    //region api calling

    @Singleton
    @Provides
    fun provideLoginAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): APIService {
        return retrofitBuilder.client(okHttpClient).build().create(APIService::class.java)
    }


    //endregion
}