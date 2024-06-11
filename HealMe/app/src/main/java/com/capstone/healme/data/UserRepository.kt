package com.capstone.healme.data

import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.remote.response.LoginResponse
import com.capstone.healme.data.remote.response.RegisterResponse
import com.capstone.healme.data.remote.retrofit.ApiService
import com.google.gson.Gson
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun registerUser(name: RequestBody, email: RequestBody, password: RequestBody): RegisterResponse {
        return try {
            apiService.signUp(email,password, name)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, RegisterResponse::class.java)
        }
//        catch (e: IOException) {
//            Log.d("error", e.message!!)
//        } catch (e: Exception) {
//
//        }
    }

    suspend fun loginUser(email: RequestBody, password: RequestBody): LoginResponse {
        return try {
            apiService.logIn(email, password)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, LoginResponse::class.java)
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