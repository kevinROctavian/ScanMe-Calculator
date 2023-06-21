package com.kevinroctavian.scanme.data.repository

import com.kevinroctavian.scanme.data.model.ScanResult
import com.kevinroctavian.scanme.data.source.db.ScanResultDao
import com.kevinroctavian.scanme.data.source.file.ScanResultFile
import kotlinx.coroutines.flow.Flow

class ScanResultRepository(
    private val scanResultDao: ScanResultDao,
    private val scanResultFile: ScanResultFile
) {

    fun getScanResults(): Flow<List<ScanResult>> = scanResultDao.getAllItems()
    suspend fun insertScanResult(scanResult: ScanResult) =
        scanResultDao.upsertScanResult(scanResult)

    suspend fun insertScanResultFile(scanResult: ScanResult) = scanResultFile.writeScanResult(scanResult)
    fun getScanResultsFile(): Flow<List<ScanResult>> = scanResultFile.data

}