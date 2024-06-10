package com.capstone.healme.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.response.PostResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    private var _registerResponse = MutableLiveData<PostResponse>()
    val registerResponse = _registerResponse
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResponse.value = userRepository.registerUser(name, email,password)
        }
    }
}