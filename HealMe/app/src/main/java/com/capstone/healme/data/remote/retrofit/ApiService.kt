package com.capstone.healme.data.remote.retrofit

import com.capstone.healme.data.remote.requestbody.HealthcareBody
import com.capstone.healme.data.remote.response.HealthcareResponse
import com.capstone.healme.data.remote.response.LoginResponse
import com.capstone.healme.data.remote.response.ProfileResponse
import com.capstone.healme.data.remote.response.RegisterResponse
import com.capstone.healme.data.remote.response.ScanResponse
import com.capstone.healme.data.remote.response.UpdatePasswordResponse
import com.capstone.healme.data.remote.response.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET("auth/profile")
    suspend fun getProfile(): ProfileResponse

    @Multipart
    @PUT("auth/profile")
    suspend fun updateProfile(
        @Part("username") username: RequestBody,
        @Part("dateOfBirth") birthdate: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("weight") weight: RequestBody
    ): UpdateProfileResponse

    @Multipart
    @PUT("auth/update-password")
    suspend fun updatePassword(
        @Part("oldPassword") oldPassword: RequestBody,
        @Part("newPassword") newPassword: RequestBody
    ): UpdatePasswordResponse


    @Multipart
    @POST("predict/result")
    suspend fun scanImage(
        @Part file: MultipartBody.Part
    ): ScanResponse

    @POST("hospital/nearby-hospital")
    suspend fun getNearbyPlaces(
        @Body healthcareBody: HealthcareBody
    ): HealthcareResponse
}