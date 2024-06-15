package com.capstone.healme.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProfileResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null
) : Parcelable

@Parcelize
data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("__v")
	val v: Int? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,
//
//	@field:SerializedName("results")
//	val results: List<Any?>? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable
