package com.kevinroctavian.scanme.ui.analyzeimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.googlecode.tesseract.android.TessBaseAPI
import com.kevinroctavian.scanme.R
import com.kevinroctavian.scanme.ui.AppViewModelProvider
import com.kevinroctavian.scanme.ui.home.StorageSelector
import com.kevinroctavian.scanme.util.validation.ExpressionUtil
import kotlinx.coroutines.launch
import org.apache.commons.io.FileUtils
//import org.opencv.android.Utils
//import org.opencv.core.CvType
//import org.opencv.core.Mat
//import org.opencv.core.Size
//import org.opencv.imgproc.Imgproc
import java.io.File
import java.net.URLDecoder


@Composable
fun AnalyzeImageScreen(
    path: String,
    onBackPress: () -> Unit,
    viewModel: AnalyzeImageViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(onBackPress = onBackPress, uiState = viewModel.itemUiState, onSaveClick = {
            coroutineScope.launch {
                viewModel.saveItem()
                onBackPress()
            }
        })
        AnalyzeImageContent(
            path = path,
            uiState = viewModel.itemUiState,
            onValueChange = viewModel::updateUiState
        )
    }
}

@Composable
private fun TopAppBar(
    uiState: AnalyzeImageState,
    onSaveClick: () -> Unit,
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
            Text(stringResource(id = R.string.analyzing_image), textAlign = TextAlign.Center)
        }
        IconButton(onClick = onSaveClick, enabled = uiState.isScanResultValid) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add",
            )
        }
    }
}

@Composable
private fun AnalyzeImageContent(
    uiState: AnalyzeImageState,
    onValueChange: (AnalzeImageDetail) -> Unit,
    path: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        var file: File? = null
        if (path != "") file = File(path)
        Column(
            Modifier
                .padding(10.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var extractedText = remember {
                mutableStateOf("Analyzing....")
            }
            Text(extractedText.value, maxLines = 2)
            if (file != null) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(file).build(),
                    contentDescription = "image",
                    Modifier
                        .fillMaxHeight(0.7f)
                        .padding(10.dp),
                    alignment = Alignment.Center
                )
                // Firebase MLKit
                var bitmap = BitmapFactory.decodeFile(path)
                val inputImage = bitmap?.let { InputImage.fromBitmap(it, 0) }
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                if (inputImage != null) {
                    recognizer.process(inputImage)
                        .addOnSuccessListener { visionText ->
                            var info = "Text Not Found"

                            val expressions =
                                ExpressionUtil.extractArgumentAndExpresions(visionText.text)
                            if (expressions.size >= 3)
                                if (ExpressionUtil.validateArithmaticOperation(
                                        expressions[0],
                                        expressions[2],
                                        expressions[1]
                                    )
                                ) {
                                    var operand1 = expressions[0].toInt()
                                    var operand2 = expressions[2].toInt()
                                    var operator = expressions[1]
                                    var result = 0
                                    when (operator) {
                                        "+" -> result = operand1 + operand2
                                        "-" -> result = operand1 - operand2
                                        "*" -> result = operand1 * operand2
                                        "/" -> result = operand1 / operand2
                                    }
                                    info = "$operand1 $operator $operand2 = $result"
                                    onValueChange(
                                        uiState.detail.copy(
                                            operand1 = operand1,
                                            operand2 = operand2,
                                            operator = operator,
                                            result = result,
                                            image = path
                                        )
                                    )
                                }
                            extractedText.value = info
                        }
                        .addOnFailureListener {}
                }
                // Tesseract OCR
//                val context = LocalContext.current
//                LaunchedEffect(Unit) {
//                    withContext(Dispatchers.IO) {
//                        extractDigits(context, path, onExtractedText = { extractedText ->
//                            Log.d("d", "extractedText: " + extractedText)
//                        })
//                    }
//                }
            }
            Text(stringResource(id = R.string.path, path))

        }
    }
}

@Preview
@Composable
fun AnalyzeImageScreenPreview() {
}

// Tesseract OCR
//fun extractDigits(
//    context: Context,
//    path: String,
//    onExtractedText: (extractedText: String) -> Unit
//) {
//    var bitmap = BitmapFactory.decodeFile(path)
//    bitmap = Bildverarbeitung(bitmap)
//    val tessBaseApi = TessBaseAPI()
//    val inputStream = context.assets.open("eng.traineddata")
//    val directory = File(context.filesDir.path + "/tessdata/")
//    directory.mkdirs()
//    val trainDataFile = File(directory.path, "eng.traineddata")
//    FileUtils.copyToFile(inputStream, trainDataFile);
//    tessBaseApi.init(context.filesDir.path, "eng")
//    tessBaseApi.setImage(bitmap)
//    val extractedText = URLDecoder.decode(tessBaseApi.utF8Text, "UTF-8")
//    Log.d("d", "extractedText: " + extractedText)
//    onExtractedText(extractedText)
//    tessBaseApi.end()
//}
//
//fun Bildverarbeitung(image: Bitmap): Bitmap? {
//    val tmp = Mat(image.width, image.height, CvType.CV_8UC1)
//    Utils.bitmapToMat(image, tmp)
//    Imgproc.cvtColor(tmp, tmp, Imgproc.COLOR_RGB2GRAY)
//    Imgproc.GaussianBlur(tmp, tmp, Size(3.0, 3.0), 0.0)
//    Imgproc.threshold(tmp, tmp, 0.0, 255.0, Imgproc.THRESH_OTSU)
//    Utils.matToBitmap(tmp, image)
//    return image
//}