package com.capstone.healme.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.LoginResponse
import kotlinx.coroutines.launch
import okhttp3.RequestBody

class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse = _loginResponse

    fun login(email: RequestBody, password: RequestBody) {
        viewModelScope.launch {
            _loginResponse.value = userRepository.loginUser(email, password)
        }
    }

    fun setUserLogin(token: String, id: String) {
        viewModelScope.launch {
            userRepository.setUserLogin(token, id)
        }
    }
}