package com.capstone.healme.data.remote.retrofit

import com.capstone.healme.data.remote.response.PostResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("signup")
    suspend fun signUp(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") username: String
    ): PostResponse
}