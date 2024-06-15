package com.capstone.healme.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.local.room.HistoryDao
import com.capstone.healme.data.remote.response.LoginResponse
import com.capstone.healme.data.remote.response.RegisterResponse
import com.capstone.healme.data.remote.response.ScanResponse
import com.capstone.healme.data.remote.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val historyDao: HistoryDao
) {

    suspend fun registerUser(
        name: RequestBody,
        birthDate: RequestBody,
        gender: RequestBody,
        weight: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): RegisterResponse {
        return try {
            apiService.signUp(name, birthDate, gender, weight, email, password)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, RegisterResponse::class.java)
        } catch (e: IOException) {
            Log.d("IOException", e.message!!)
            RegisterResponse(error = true, message = e.message)
        } catch (e: Exception) {
            RegisterResponse(error = true, message = "Unknown Error")
        }
    }

    suspend fun loginUser(email: RequestBody, password: RequestBody): LoginResponse {
        return try {
            apiService.login(email, password)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, LoginResponse::class.java)
        } catch (e: IOException) {
            LoginResponse(error = true, message = e.message)
        } catch (e: Exception) {
            LoginResponse(error = true)
        }
    }

    suspend fun scanImage(image: MultipartBody.Part): ScanResponse {
        return try {
            apiService.scanImage(image)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, ScanResponse::class.java)
        } catch (e: IOException) {
            ScanResponse(error = true, message = e.message)
        } catch (e: Exception) {
            ScanResponse(error = true, message = e.message)
        }
    }

    suspend fun setUserLogin(token: String, id: String) {
        userPreferences.setUserLogin(token, id)
    }

    suspend fun setUserLogout() {
        userPreferences.setUserLogout()
    }

    fun getUserState(): LiveData<Boolean> {
        return userPreferences.checkLoginStatus().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
            historyDao: HistoryDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreferences, historyDao)
            }.also { instance = it }
    }
}