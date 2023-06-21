package com.kevinroctavian.scanme.ui.analyzeimage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.kevinroctavian.scanme.data.preferences.DataPreferences
import com.kevinroctavian.scanme.data.model.ScanResult
import com.kevinroctavian.scanme.data.repository.ScanResultRepository
import com.kevinroctavian.scanme.ui.home.StorageSelector


class AnalyzeImageViewModel(private val scanResultRepository: ScanResultRepository) : ViewModel() {

    var itemUiState by mutableStateOf(AnalyzeImageState())
        private set

    fun updateUiState(analzeImageDetail: AnalzeImageDetail) {
        itemUiState =
            AnalyzeImageState(
                detail = analzeImageDetail,
                isScanResultValid = validateInput(analzeImageDetail)
            )
    }

    suspend fun saveItem() {
        if (validateInput()) {
            if (DataPreferences.getStorageType() == StorageSelector.DATABASE)
                scanResultRepository.insertScanResult(itemUiState.detail.toScanResult())
            else scanResultRepository.insertScanResultFile(itemUiState.detail.toScanResult())
        }
    }

    private fun validateInput(uiState: AnalzeImageDetail = itemUiState.detail): Boolean {
        return with(uiState) {
            operator.isNotBlank() && image.isNotBlank()
        }
    }
}

data class AnalyzeImageState(
    val detail: AnalzeImageDetail = AnalzeImageDetail(),
    val isScanResultValid: Boolean = false
)

data class AnalzeImageDetail(
    val operand1: Int = 0,
    val operand2: Int = 0,
    val operator: String = "",
    val result: Int = 0,
    val image: String = ""
)

fun AnalzeImageDetail.toScanResult(): ScanResult = ScanResult(
    operand1 = operand1,
    operand2 = operand2,
    operator = operator,
    result = result,
    image = image
)