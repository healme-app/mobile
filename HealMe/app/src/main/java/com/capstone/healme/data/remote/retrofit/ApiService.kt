package com.capstone.healme.data.remote.retrofit

import com.capstone.healme.data.remote.response.LoginResponse
import com.capstone.healme.data.remote.response.RegisterResponse
import com.capstone.healme.data.remote.response.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @PUT("auth/signup")
    suspend fun signUp(
        @Part("username") username: RequestBody,
        @Part("dateOfBirth") birthdate: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): RegisterResponse

    @Multipart
    @POST("auth/login")
    suspend fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): LoginResponse

    @Multipart
    @POST("predict/result")
    suspend fun scanImage(
        @Part file: MultipartBody.Part
    ): ScanResponse
}