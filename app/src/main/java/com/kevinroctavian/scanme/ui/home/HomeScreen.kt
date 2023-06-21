package com.kevinroctavian.scanme.ui.home

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kevinroctavian.scanme.BuildConfig
import com.kevinroctavian.scanme.R
import com.kevinroctavian.scanme.data.model.ScanResult
import com.kevinroctavian.scanme.ui.AppViewModelProvider
import com.kevinroctavian.scanme.util.provider.ScanMeFileProvider
import com.kevinroctavian.scanme.util.ext.rotateImage
import com.kevinroctavian.scanme.util.ext.saveImage
import java.io.File

@Composable
fun HomeScreen(
    navigateToScanResultDetail: (ScanResult) -> Unit,
    navigateToAnalyzingImage: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scanResults by viewModel.scanResults.collectAsState(
        initial = emptyList()
    )
    val storageSelector by viewModel.storageSelector.collectAsState()
    HomeContent(
        scanResults = scanResults,
        navigateToScanResultDetail = navigateToScanResultDetail,
        onPictureSaved = navigateToAnalyzingImage,
        storageSelector = storageSelector,
        onStorageOptionSelected = viewModel::updateStorageSelector
    )
}

@Composable
fun HomeContent(
    scanResults: List<ScanResult>,
    navigateToScanResultDetail: (ScanResult) -> Unit,
    onPictureSaved: (String) -> Unit,
    storageSelector: StorageSelector,
    onStorageOptionSelected: (StorageSelector) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        Column(modifier.fillMaxSize()) {
            ScanResultList(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f),
                scanResults = scanResults,
                navigateToScanResultDetail = navigateToScanResultDetail
            )
            Column {
                AddInput(onPictureSaved = onPictureSaved, modifier)
                UserInputStorageSelector(
                    storageSelector = storageSelector,
                    onStorageOptionSelected = onStorageOptionSelected, modifier = modifier
                )
            }
        }
    }
}

@Composable
fun ScanResultList(
    scanResults: List<ScanResult>,
    navigateToScanResultDetail: (ScanResult) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier.fillMaxSize().testTag("List Scan Results")) {
        for (scanResult in scanResults) {
            item {
                ScanResultListItem(scanResult, navigateToScanResultDetail)
            }
        }
    }
}

@Composable
fun ScanResultListItem(
    scanResult: ScanResult,
    onScanResultClicked: (ScanResult) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .border(width = 2.dp, color = Color.Black)
            .padding(10.dp)
            .clickable { onScanResultClicked(scanResult) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val file = File(scanResult.image)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(file).build(),
            contentDescription = "image",
            Modifier
                .width(55.dp)
                .height(50.dp)
                .padding(end = 10.dp),
            alignment = Alignment.Center
        )
        Column {
            Text(
                stringResource(
                    R.string.input,
                    scanResult.operand1,
                    scanResult.operator,
                    scanResult.operand2
                )
            )
            Text(stringResource(R.string.result, scanResult.result))
        }
        Spacer(modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "next"
        )
    }
}

@Composable
fun AddInput(
    onPictureSaved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var tempPhotoUri = Uri.EMPTY

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                tempPhotoUri.path?.let { File(context.cacheDir, it).path }
                    ?.let { onPictureSaved(it) }
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            tempPhotoUri = ScanMeFileProvider.getImageUri(context)
            cameraLauncher.launch(tempPhotoUri)
        }
    }

    val imagePicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            var bitmap: Bitmap?
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = uri?.let {
                    ImageDecoder
                        .createSource(context.contentResolver, it)
                }
                bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
            }
            if (bitmap != null) {
                if (bitmap.width > bitmap.height) {
                    bitmap = bitmap.rotateImage(90.0f)
                }
                bitmap.saveImage(context)?.let { onPictureSaved(it) }
            }
        }

    val permissionCheckResult =
        ContextCompat.checkSelfPermission(LocalContext.current, android.Manifest.permission.CAMERA)
    Button(
        modifier = modifier.testTag("Button Add Input")
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp), onClick = {
            if (BuildConfig.BUILD_TYPE == "built-in-camera") {
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    tempPhotoUri = ScanMeFileProvider.getImageUri(context)
                    cameraLauncher.launch(tempPhotoUri)
                } else permissionLauncher.launch(android.Manifest.permission.CAMERA)
            } else imagePicker.launch("image/*")
        }) {
        Text(stringResource(R.string.add_input))
    }
}

@Composable
fun UserInputStorageSelector(
    onStorageOptionSelected: (StorageSelector) -> Unit,
    storageSelector: StorageSelector,
    modifier: Modifier = Modifier
) {
    val storageOptions = listOf(
        stringResource(R.string.use_file_storage),
        stringResource(R.string.use_database_storage)
    )
    Log.d("d", "Storage DATABASE: " + (storageSelector == StorageSelector.DATABASE))
    var index = if (storageSelector == StorageSelector.DATABASE) 1 else 0
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf(storageOptions[index])
    }

    Column(
        modifier
            .fillMaxWidth(1.0f)
            .padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        storageOptions.forEach { text ->
            Row(
                modifier.testTag(text)
                    .height(30.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text)
                RadioButton(
                    selected = (text.equals(selectedOption, true)),
                    onClick = {
                        onOptionSelected(text)
                        if (text == storageOptions[1]) onStorageOptionSelected(StorageSelector.DATABASE)
                        else onStorageOptionSelected(StorageSelector.FILE)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary,
                        unselectedColor = MaterialTheme.colors.primary.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ScanResultsPreview() {
}
