package com.capstone.healme.di

import android.content.Context
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.local.datastore.dataStore
import com.capstone.healme.data.local.room.HistoryRoomDatabase
import com.capstone.healme.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = UserPreferences.getInstance(context.applicationContext.dataStore)
        val database = HistoryRoomDatabase.getDatabase(context)
        val dao = database.historyDao()
        val tokenFlow = preferences.getUserToken()
        val apiService = ApiConfig.getApiService(tokenFlow)
        return UserRepository.getInstance(apiService, preferences, dao)
    }
}