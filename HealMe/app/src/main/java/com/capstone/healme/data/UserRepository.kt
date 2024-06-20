package com.capstone.healme.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.capstone.healme.data.local.datastore.UserPreferences
import com.capstone.healme.data.local.entity.ScanEntity
import com.capstone.healme.data.local.room.HistoryDao
import com.capstone.healme.data.remote.requestbody.HealthcareBody
import com.capstone.healme.data.remote.response.GeminiResponse
import com.capstone.healme.data.remote.response.HealthcareResponse
import com.capstone.healme.data.remote.response.LoginResponse
import com.capstone.healme.data.remote.response.ProfileResponse
import com.capstone.healme.data.remote.response.RegisterResponse
import com.capstone.healme.data.remote.response.ScanResponse
import com.capstone.healme.data.remote.response.UpdatePasswordResponse
import com.capstone.healme.data.remote.response.UpdateProfileResponse
import com.capstone.healme.data.remote.retrofit.ApiService
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.UnknownException
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.IOException

class UserRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
    private val historyDao: HistoryDao,
    private val geminiModel: GenerativeModel
) {

    suspend fun registerUser(
        name: RequestBody,
        birthDate: RequestBody,
        gender: RequestBody,
        weight: RequestBody,
        email: RequestBody,
        password: RequestBody
    ): RegisterResponse {
        return try {
            apiService.signUp(name, birthDate, gender, weight, email, password)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, RegisterResponse::class.java)
        } catch (e: IOException) {
            RegisterResponse(error = true, message = e.message)
        } catch (e: Exception) {
            RegisterResponse(error = true, message = e.message)
        }
    }

    suspend fun loginUser(email: RequestBody, password: RequestBody): LoginResponse {
        return try {
            apiService.login(email, password)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, LoginResponse::class.java)
        } catch (e: IOException) {
            LoginResponse(error = true, message = e.message)
        } catch (e: Exception) {
            LoginResponse(error = true, message = e.message)
        }
    }

    suspend fun getUserProfile(): ProfileResponse {
        return try {
            apiService.getProfile()
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, ProfileResponse::class.java)
        } catch (e: IOException) {
            ProfileResponse(error = true, message = e.message)
        } catch (e: Exception) {
            ProfileResponse(error = true, message = e.message)
        }
    }

    suspend fun updateUserProfile(
        name: RequestBody,
        birthDate: RequestBody,
        gender: RequestBody,
        weight: RequestBody
    ): UpdateProfileResponse {
        return try {
            apiService.updateProfile(name, birthDate, gender, weight)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, UpdateProfileResponse::class.java)
        } catch (e: IOException) {
            UpdateProfileResponse(error = true, message = e.message)
        } catch (e: Exception) {
            UpdateProfileResponse(error = true, message = e.message)
        }
    }

    suspend fun updatePassword(
        oldPassword: RequestBody,
        newPassword: RequestBody
    ): UpdatePasswordResponse {
        return try {
            apiService.updatePassword(oldPassword, newPassword)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, UpdatePasswordResponse::class.java)
        } catch (e: IOException) {
            UpdatePasswordResponse(error = true, message = e.message)
        } catch (e: Exception) {
            UpdatePasswordResponse(error = true, message = e.message)
        }
    }

    suspend fun scanImage(image: MultipartBody.Part): ScanResponse {
        return try {
            apiService.scanImage(image)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, ScanResponse::class.java)
        } catch (e: IOException) {
            ScanResponse(error = true, message = e.message)
        } catch (e: Exception) {
            ScanResponse(error = true, message = e.message)
        }
    }

    suspend fun getNearbyHealthcare(healthcareBody: HealthcareBody): HealthcareResponse {
        return try {
            apiService.getNearbyPlaces(healthcareBody)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            Gson().fromJson(jsonInString, HealthcareResponse::class.java)
        } catch (e: IOException) {
            HealthcareResponse(error = true, message = e.message)
        } catch (e: Exception) {
            HealthcareResponse(error = true, message = e.message)
        }
    }

    suspend fun addHistory(scanEntity: ScanEntity) {
        historyDao.insertResult(scanEntity)
    }

    fun getAllHistory(): LiveData<List<ScanEntity>> {
        return historyDao.getAllHistory()
    }

    fun getDetailHistory(resultId: String): LiveData<ScanEntity> {
        return historyDao.getDetailHistory(resultId)
    }

    suspend fun setUserLogin(token: String, id: String) {
        userPreferences.setUserLogin(token, id)
    }

    suspend fun setUserLogout() {
        userPreferences.setUserLogout()
    }

    fun getUserState(): LiveData<Boolean> {
        return userPreferences.checkLoginStatus().asLiveData()
    }

    suspend fun setFirstOpen() {
        userPreferences.setFirstOpen()
    }

    fun getFirstOpen(): LiveData<Boolean> {
        return userPreferences.getFirstOpen().asLiveData()
    }

    suspend fun getResponseResult(prompt: String): GeminiResponse {
        return try {
            val geminiResponseRaw = geminiModel.generateContent(prompt).text
            val geminiResponseJson = geminiResponseRaw?.replace("*", "")!!.replace("#", "")
            Gson().fromJson(geminiResponseJson, GeminiResponse::class.java)
        } catch (e: ServerException) {
            GeminiResponse(error = true)
        } catch (e: UnknownException) {
            GeminiResponse(error = true)
        } catch (e: GoogleGenerativeAIException) {
            GeminiResponse(error = true)
        }
    }

    suspend fun getResponseTips(prompt: String): String {
        return try {
            geminiModel.generateContent(prompt).text!!.replace("*", "")
        } catch (e: ServerException) {
            "Ada kesalahan pada server, coba lagi!"
        } catch (e: UnknownException) {
            "Ada kesalahan pada server, coba lagi!"
        } catch (e: GoogleGenerativeAIException) {
            "Ada kesalahan pada server, coba lagi!"
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
            historyDao: HistoryDao,
            geminiModel: GenerativeModel
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreferences, historyDao, geminiModel)
            }.also { instance = it }
    }
}