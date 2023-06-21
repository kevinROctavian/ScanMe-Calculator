package com.kevinroctavian.scanme.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kevinroctavian.scanme.ScanMeApplication
import com.kevinroctavian.scanme.ui.analyzeimage.AnalyzeImageViewModel
import com.kevinroctavian.scanme.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(application().container.scanResultRepository)
        }
        initializer {
            AnalyzeImageViewModel(application().container.scanResultRepository)
        }
    }
}

fun CreationExtras.application(): ScanMeApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ScanMeApplication)