package com.capstone.healme.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "scan")
@Parcelize
data class ScanEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo
    var resultId: String = "",

    @ColumnInfo
    var result: String? = null,

    @ColumnInfo
    var confidenceScore: Float? = null,

    @ColumnInfo
    var imageUrl: String? = null,

    @ColumnInfo
    var createdAt: String? = null

): Parcelable
