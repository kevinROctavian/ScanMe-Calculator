package com.kevinroctavian.scanme.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinroctavian.scanme.data.preferences.DataPreferences
import com.kevinroctavian.scanme.data.model.ScanResult
import com.kevinroctavian.scanme.data.repository.ScanResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

data class HomeViewState(
    var scanResults: List<ScanResult> = emptyList(),
    var storageSelector: StorageSelector = StorageSelector.DATABASE
)

class HomeViewModel(private val scanResultRepository: ScanResultRepository): ViewModel(){

    val storageSelector = MutableStateFlow(DataPreferences.getStorageType())
    val scanResults = storageSelector
        .flatMapLatest { storageSelector ->
            when(storageSelector){
                StorageSelector.DATABASE -> scanResultRepository.getScanResults()
                StorageSelector.FILE -> scanResultRepository.getScanResultsFile()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun updateStorageSelector(storageType: StorageSelector){
        storageSelector.value = storageType
        DataPreferences.saveStorageType(storageType)
    }
}

enum class StorageSelector{
    DATABASE, FILE
}