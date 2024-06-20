package com.capstone.healme.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class UpdatePasswordResponse(

	@field:SerializedName("error")
	val error: Boolean = true,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
