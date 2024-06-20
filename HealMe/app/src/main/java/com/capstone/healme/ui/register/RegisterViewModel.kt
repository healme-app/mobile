package com.capstone.healme.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse = _registerResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading
    fun registerUser(
        name: RequestBody,
        birthDate: RequestBody,
        gender: RequestBody,
        weight: RequestBody,
        email: RequestBody,
        password: RequestBody
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            _registerResponse.value =
                userRepository.registerUser(name, birthDate, gender, weight, email, password)
            _isLoading.value = false
        }
    }
}