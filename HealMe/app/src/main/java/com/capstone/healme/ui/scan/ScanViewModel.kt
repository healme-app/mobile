package com.capstone.healme.ui.scan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.ScanResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ScanViewModel(private val userRepository: UserRepository): ViewModel() {
    private var _scanResponse = MutableLiveData<ScanResponse>()
    val scanResponse = _scanResponse

    fun scanImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            _scanResponse.value = userRepository.scanImage(image)
        }
    }
}