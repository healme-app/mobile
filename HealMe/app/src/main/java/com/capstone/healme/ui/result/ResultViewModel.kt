package com.capstone.healme.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.entity.ScanEntity
import com.capstone.healme.data.remote.response.GeminiResponse
import com.capstone.healme.data.remote.response.ProfileResponse
import kotlinx.coroutines.launch

class ResultViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _geminiResponse = MutableLiveData<GeminiResponse>()
    val geminiResponse = _geminiResponse

    private var _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse = _profileResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading
    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            _profileResponse.value = userRepository.getUserProfile()
        }
    }

    fun getResponse(prompt: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _geminiResponse.value = userRepository.getResponseResult(prompt)
            _isLoading.value = false
        }
    }

    fun getDetailHistory(resultId: String): LiveData<ScanEntity> {
        return userRepository.getDetailHistory(resultId)
    }


}