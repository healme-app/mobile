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
    private val userId = stringPreferencesKey("user_id")
    private val firstOpen = booleanPreferencesKey("first_open")

    suspend fun setUserLogin(token: String, id: String) {
        dataStore.edit { preferences ->
            preferences[isLogin] = true
            preferences[userToken] = token
            preferences[userId] = id
        }
    }


    suspend fun setUserLogout() {
        dataStore.edit { preferences ->
            preferences[isLogin] = false
            preferences[userToken] = ""
            preferences[userId] = ""
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

    suspend fun setFirstOpen() {
        dataStore.edit { preferences ->
            preferences[firstOpen] = false
        }
    }

    fun getFirstOpen(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[firstOpen] ?: true
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