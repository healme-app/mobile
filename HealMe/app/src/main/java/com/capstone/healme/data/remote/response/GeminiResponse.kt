package com.capstone.healme.data.remote.response

import com.google.gson.annotations.SerializedName

data class GeminiResponse (
    @SerializedName("error")
    val error: Boolean? = null,

    @SerializedName("explanation")
    val explanation: String? = null,

    @SerializedName("firstAidRecommendation")
    val firstAidRecommendation: String? = null
)