package com.capstone.healme.ui.healthcare

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.healme.data.UserRepository
import com.capstone.healme.data.remote.requestbody.HealthcareBody
import com.capstone.healme.data.remote.response.HealthcareResponse
import kotlinx.coroutines.launch

class HealthcareViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _healthcareResponse = MutableLiveData<HealthcareResponse>()
    val healthcareResponse = _healthcareResponse

    private var _latLong = MutableLiveData<List<Double>>()
    val latLong = _latLong

    fun getNearbyHealthcare(healthcareBody: HealthcareBody) {
        viewModelScope.launch {
            _healthcareResponse.value = userRepository.getNearbyHealthcare(healthcareBody)
        }
    }

    fun setLatLong(lat: Double, lon: Double) {
        _latLong.value = listOf(lat, lon)
    }
}