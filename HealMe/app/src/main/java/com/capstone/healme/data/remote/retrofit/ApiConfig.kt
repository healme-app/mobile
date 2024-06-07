package com.capstone.healme.data.remote.retrofit

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("BuildConfig.BASE_URL")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}