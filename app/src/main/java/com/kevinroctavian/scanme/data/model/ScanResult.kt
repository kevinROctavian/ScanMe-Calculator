package com.kevinroctavian.scanme.data.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScanResult(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "operand1") val operand1: Int,
    @ColumnInfo(name = "operand2") val operand2: Int,
    @ColumnInfo(name = "operator") val operator: String,
    @ColumnInfo(name = "result") val result: Int,
    @ColumnInfo(name = "image") val image: String
)