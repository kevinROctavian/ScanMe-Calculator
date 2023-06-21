package com.kevinroctavian.scanme.data.source.file

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kevinroctavian.scanme.data.model.ScanResult
import com.kevinroctavian.scanme.util.encryption.AESEncyption
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter


class ScanResultFile(
    context: Context
) {
    private val jsonFile = File(context.filesDir, "scanme")

    var data =  getAllItems()

    suspend fun writeScanResult(scanResult: ScanResult) {
        try {
            val scanResults = ArrayList<ScanResult>()
            val d = data.flatMapConcat { it.asFlow() }.toList()
            if (d.isNotEmpty()) scanResults.addAll(d)
            scanResults.add(scanResult)
            val jsonArray = Gson().toJsonTree(scanResults).asJsonArray
            PrintWriter(FileWriter(jsonFile)).use {
                it.write(AESEncyption.encrypt(jsonArray.toString()))
            }
            delay(150L)
            data = flow { emit(scanResults) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAllItems(): Flow<List<ScanResult>> {
        var scanResults: List<ScanResult> = emptyList()
        if (jsonFile.exists()) {
            val bufferedReader: BufferedReader = jsonFile.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            val gson = GsonBuilder().create()
            scanResults = gson.fromJson(AESEncyption.decrypt(inputString), Array<ScanResult>::class.java).toList()
        }
        return flow { emit(scanResults) }
    }

}