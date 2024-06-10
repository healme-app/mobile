package com.capstone.healme.data

import androidx.lifecycle.LiveData
import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.remote.response.PostResponse
import com.capstone.healme.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun registerUser(name: String, email: String, password: String): PostResponse {
        return try {
            apiService.signUp(email,password, name)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, PostResponse::class.java)
        } catch (e: IOException) {
            PostResponse(error = true, message = "No Signal")
        } catch (e: Exception) {
            PostResponse(error = true, message = "Unknown Error")
        }
    }

    suspend fun setUserLogin(token: String) {
        userPreferences.setUserLogin(token)
    }

    suspend fun setUserLogout() {
        userPreferences.setUserLogout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreferences)
            }.also { instance = it }
    }
}