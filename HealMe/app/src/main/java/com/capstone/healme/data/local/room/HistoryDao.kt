package com.capstone.healme.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.healme.data.local.entity.ScanEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertResult(scanEntity: ScanEntity)

    @Query("DELETE FROM scan")
    suspend fun deleteHistory()

    @Query("select * from scan")
    fun getAllUsers(): LiveData<List<ScanEntity>>
}