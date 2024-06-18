package com.capstone.healme.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean = true,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("data")
    val data: List<ErrorDetail>? = null
) : Parcelable

@Parcelize
data class ErrorDetail(
    @SerializedName("type")
    val type: String? = null,

    @SerializedName("value")
    val value: String? = null,

    @SerializedName("msg")
    val message: String? = null,

    @SerializedName("path")
    val path: String? = null,

    @SerializedName("location")
    val location: String? = null
) : Parcelable
