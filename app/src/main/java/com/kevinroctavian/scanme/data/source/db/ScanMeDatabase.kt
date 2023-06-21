package com.kevinroctavian.scanme.data.source.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kevinroctavian.scanme.data.model.ScanResult

@Database(entities = [ScanResult::class], version = 1, exportSchema = false)
abstract class ScanMeDatabase : RoomDatabase() {

    abstract val dao: ScanResultDao

    companion object {
        @Volatile
        private var Instance: ScanMeDatabase? = null

        fun getDatabase(context: Context): ScanMeDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScanMeDatabase::class.java, "scanme_database")
                    .build().also { Instance = it }
            }
        }
    }
}