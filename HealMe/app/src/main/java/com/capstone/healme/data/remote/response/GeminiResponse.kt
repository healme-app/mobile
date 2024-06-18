package com.capstone.healme.data.remote.response

import com.google.gson.annotations.SerializedName

data class GeminiResponse (
    @SerializedName("explanation")
    val explanation: String,

    @SerializedName("firstAidRecommendation")
    val firstAidRecommendation: String
)