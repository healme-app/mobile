package com.capstone.healme.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ScanResponse(

	@field:SerializedName("resultDb")
	val resultDb: ResultDb? = null,

	@field:SerializedName("error")
	val error: Boolean = true,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
) : Parcelable

@Parcelize
data class ResultDb(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Float? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("__v")
	val v: Int? = null,

	@field:SerializedName("firstAidRecommendation")
	val firstAidRecommendation: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("explanation")
	val explanation: String? = null,

	@field:SerializedName("user")
	val user: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
