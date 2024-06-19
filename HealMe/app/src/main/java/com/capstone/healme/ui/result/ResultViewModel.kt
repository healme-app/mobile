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
    private var _geminiResponse = MutableLiveData<GeminiResponse>()
    val geminiResponse = _geminiResponse
    fun getResponse(prompt: String) {
        viewModelScope.launch {
            _geminiResponse.value = userRepository.getResponseResult(prompt)
        }
    }

    fun getDetailHistory(resultId: String): LiveData<ScanEntity> {
        return userRepository.getDetailHistory(resultId)
    }
}