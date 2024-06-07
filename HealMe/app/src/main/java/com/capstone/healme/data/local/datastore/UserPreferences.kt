package com.capstone.healme.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "users")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>){
    private val isLogin = booleanPreferencesKey("is_login")
    private val userToken = stringPreferencesKey("user_token")

    suspend fun setUserLogin(token: String) {
        dataStore.edit { preferences ->
            preferences[isLogin] = true
            preferences[userToken] = token
        }
    }

    suspend fun setUserLogout() {
        dataStore.edit { preferences ->
            preferences[isLogin] = false
            preferences[userToken] = ""
        }
    }

    fun checkLoginStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isLogin] ?: false
        }
    }

    fun getUserToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[userToken] ?: ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}