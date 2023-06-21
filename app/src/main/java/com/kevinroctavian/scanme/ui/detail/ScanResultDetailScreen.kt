package com.kevinroctavian.scanme.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kevinroctavian.scanme.R
import com.kevinroctavian.scanme.data.model.ScanResult
import java.io.File

@Composable
fun ScanResultDetailScreen(
    onBackPress: () -> Unit, scanResult: ScanResult,
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(onBackPress = onBackPress)
        ScanResultDetailContent(scanResult = scanResult)
    }
}

@Composable
private fun TopAppBar(
    onBackPress: () -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        IconButton(onClick = onBackPress) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "back"
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .height(45.dp)
        ) {
            Text(
                stringResource(id = R.string.scan_result_detail),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ScanResultDetailContent(
    scanResult: ScanResult
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var file: File? = null
        if (scanResult.image != "") file = File(scanResult.image)
        Column(
            Modifier
                .padding(10.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(
                    R.string.input,
                    scanResult.operand1,
                    scanResult.operator,
                    scanResult.operand2
                )
            )
            Text(stringResource(R.string.result, scanResult.result))
            if (file != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(file).build(),
                    contentDescription = "image",
                    Modifier
                        .fillMaxHeight(0.7f)
                        .padding(10.dp),
                    alignment = Alignment.Center
                )
            }
            Text(stringResource(id = R.string.path, scanResult.image))

        }
    }
}