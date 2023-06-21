package com.kevinroctavian.scanme.data

import android.content.Context
import com.kevinroctavian.scanme.data.repository.ScanResultRepository
import com.kevinroctavian.scanme.data.source.db.ScanMeDatabase
import com.kevinroctavian.scanme.data.source.file.ScanResultFile

interface AppContainer {
    val scanResultRepository: ScanResultRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val scanResultRepository: ScanResultRepository by lazy {
        ScanResultRepository(ScanMeDatabase.getDatabase(context).dao, ScanResultFile(context))
    }

}