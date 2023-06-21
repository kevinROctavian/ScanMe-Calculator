package com.kevinroctavian.scanme.data.source.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kevinroctavian.scanme.data.model.ScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanResultDao {

    @Upsert
    suspend fun upsertScanResult(scanResult: ScanResult)

    @Query("SELECT * from ScanResult ORDER BY id ASC")
    fun getAllItems(): Flow<List<ScanResult>>
}