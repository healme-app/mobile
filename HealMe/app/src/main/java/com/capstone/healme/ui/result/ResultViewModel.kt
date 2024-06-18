package com.capstone.healme.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.BuildConfig
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.entity.ScanEntity
import com.capstone.healme.data.remote.response.GeminiResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ResultViewModel(private val userRepository: UserRepository): ViewModel() {
    private lateinit var geminiModel: GenerativeModel
    private var _geminiResponse = MutableLiveData<GeminiResponse>()
    val geminiResponse = _geminiResponse

    init {
        setupGemini()
    }
    private fun setupGemini() {
        val dangerSafety = SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE)
        val sexualSafety = SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE)

        geminiModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
            safetySettings = listOf(dangerSafety, sexualSafety)
        )
    }

    fun getResponse(prompt: String) {
//        viewModelScope.launch {
//            val geminiResponseRaw = geminiModel.generateContent(prompt).text
//            val geminiResponseJson = geminiResponseRaw?.replace("*", "")
//            Log.d("gemini response json", geminiResponseRaw!!)
//            _geminiResponse.value = Gson().fromJson(geminiResponseJson, GeminiResponse::class.java)
//        }

        viewModelScope.launch {
            val geminiResponse = geminiModel.generateContent(prompt).text
            Log.d("response", geminiResponse!!)
        }
    }

    fun getDetailHistory(resultId: String): LiveData<ScanEntity> {
        return userRepository.getDetailHistory(resultId)
    }
}