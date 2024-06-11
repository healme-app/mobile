package com.capstone.healme.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RegisterResponse(
	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
