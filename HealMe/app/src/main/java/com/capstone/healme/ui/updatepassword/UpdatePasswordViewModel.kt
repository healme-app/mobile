package com.capstone.healme.ui.updatepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.UpdatePasswordResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class UpdatePasswordViewModel(private val userRepository: UserRepository): ViewModel() {
    private var _updateResponse = MutableLiveData<UpdatePasswordResponse>()
    val updateResponse = _updateResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun updatePassword(oldPassword: RequestBody, newPassword: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            _updateResponse.value =userRepository.updatePassword(oldPassword, newPassword)
            _isLoading.value = false
        }
    }
}