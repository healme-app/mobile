package com.capstone.healme.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse = _loginResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    fun login(email: RequestBody, password: RequestBody) {
        _isLoading.value = true
        viewModelScope.launch {
            _loginResponse.value = userRepository.loginUser(email, password)
            _isLoading.value = false
        }
    }

    fun setUserLogin(token: String, id: String) {
        viewModelScope.launch {
            userRepository.setUserLogin(token, id)
        }
    }
}