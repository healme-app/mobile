package com.capstone.healme.data.remote.retrofit

import com.capstone.healme.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        fun getApiService(tokenFlow: Flow<String>): ApiService {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestBuilder = req.newBuilder()
                val token = runBlocking { tokenFlow.first() }
                requestBuilder.addHeader("Authorization", "Bearer $token")
                val requestHeaders = requestBuilder.build()
                chain.proceed(requestHeaders)
            }

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .retryOnConnectionFailure(true)
                .build()
//            val client = OkHttpClient.Builder()
//                .addInterceptor(authInterceptor)
//                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}