package com.capstone.healme.data.remote.retrofit

import com.capstone.healme.data.remote.response.LoginResponse
import com.capstone.healme.data.remote.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @PUT("auth/signup")
    suspend fun signUp(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("username") username: RequestBody
    ): RegisterResponse

    @Multipart
    @POST("auth/login")
    suspend fun logIn(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): LoginResponse
}