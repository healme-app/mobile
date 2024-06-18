package com.capstone.healme.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.ProfileResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    private var _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse = _profileResponse

    init {
        getUserProfile()
    }
    fun getUserProfile() {
        viewModelScope.launch {
            _profileResponse.value = userRepository.getUserProfile()
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            userRepository.setUserLogout()
        }
    }
}