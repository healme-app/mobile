package com.capstone.healme.di

import android.content.Context
import com.capstone.healme.BuildConfig
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.local.datastore.dataStore
import com.capstone.healme.data.local.room.HistoryRoomDatabase
import com.capstone.healme.data.remote.retrofit.ApiConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = UserPreferences.getInstance(context.applicationContext.dataStore)
        val database = HistoryRoomDatabase.getDatabase(context)
        val dao = database.historyDao()
        val tokenFlow = preferences.getUserToken()
        val apiService = ApiConfig.getApiService(tokenFlow)
        val geminiModel = setupGemini()
        return UserRepository.getInstance(apiService, preferences, dao, geminiModel)
    }

    private fun setupGemini(): GenerativeModel {
        val dangerSafety = SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE)
        val sexualSafety = SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE)

        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
            safetySettings = listOf(dangerSafety, sexualSafety)
        )
    }
}