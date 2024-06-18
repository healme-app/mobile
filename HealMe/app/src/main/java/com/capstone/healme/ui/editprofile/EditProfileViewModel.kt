package com.capstone.healme.ui.editprofile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.ProfileResponse
import com.capstone.healme.data.remote.response.UpdateProfileResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse = _profileResponse

    private var _editProfileResponse = MutableLiveData<UpdateProfileResponse>()
    val editProfileResponse = _editProfileResponse

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch {
            _profileResponse.value = userRepository.getUserProfile()
        }
    }

    fun updateProfile(
        name: RequestBody,
        birthDate: RequestBody,
        gender: RequestBody,
        weight: RequestBody,
    ) {
        viewModelScope.launch {
            _editProfileResponse.value = userRepository.updateUserProfile(name, birthDate, gender, weight)
        }
    }
}