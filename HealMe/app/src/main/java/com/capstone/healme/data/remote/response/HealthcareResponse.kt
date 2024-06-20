package com.capstone.healme.data.remote.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HealthcareResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("count")
	val count: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
) : Parcelable

@Parcelize
data class Location(

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
) : Parcelable

@Parcelize
data class RegularOpeningHours(



	@field:SerializedName("openNow")
	val openNow: Boolean? = null,

	@field:SerializedName("weekdayDescriptions")
	val weekdayDescriptions: List<String>? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("shortFormattedAddress")
	val shortFormattedAddress: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("types")
	val types: List<String?>? = null,

	@field:SerializedName("googleMapsUri")
	val googleMapsUri: String? = null,

	@field:SerializedName("displayName")
	val displayName: String? = null,

	@field:SerializedName("location")
	val location: Location? = null,

	@field:SerializedName("regularOpeningHours")
	val regularOpeningHours: RegularOpeningHours? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
