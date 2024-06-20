package com.capstone.healme.ui.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.entity.ScanEntity
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _geminiResponse = MutableLiveData<String>()
    val geminiResponse = _geminiResponse

    private var _loadingRecentScan = MutableLiveData<Boolean>()
    val loadingRecentScan = _loadingRecentScan

    private var _loadingHealthTips = MutableLiveData<Boolean>()
    val loadingHealthTips = _loadingHealthTips

    private var _histories = MediatorLiveData<List<ScanEntity>>()
    val histories = _histories

    init {
        getAllHistories()
    }

    fun getGeminiTips(prompt: String) {
        _loadingHealthTips.value = true
        viewModelScope.launch {
            _geminiResponse.value = userRepository.getResponseTips(prompt)
            _loadingHealthTips.value = false
        }
    }

    fun getAllHistories() {
        _loadingRecentScan.value = true

        val source = userRepository.getAllHistory()
        _histories.addSource(source) { data ->
            _histories.value = data
            _loadingRecentScan.value = false
            _histories.removeSource(source)
        }
    }
}