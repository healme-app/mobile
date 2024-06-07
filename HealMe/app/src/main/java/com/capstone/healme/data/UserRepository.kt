package com.capstone.healme.data

import androidx.lifecycle.LiveData
import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.remote.retrofit.ApiService

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {
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