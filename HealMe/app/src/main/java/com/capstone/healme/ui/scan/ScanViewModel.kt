package com.capstone.healme.ui.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.local.entity.ScanEntity
import com.capstone.healme.data.remote.response.ScanResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ScanViewModel(private val userRepository: UserRepository): ViewModel() {
    private var _scanResponse = MutableLiveData<ScanResponse>()
    val scanResponse = _scanResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading
    fun scanImage(image: MultipartBody.Part) {
        _isLoading.value = true
        viewModelScope.launch {
            _scanResponse.value = userRepository.scanImage(image)
            _isLoading.value = false
        }
    }

    fun addHistory(scanEntity: ScanEntity) {
        viewModelScope.launch {
            userRepository.addHistory(scanEntity)
        }
    }

    fun clearViewModel() {
        _scanResponse.value = ScanResponse()
    }
}