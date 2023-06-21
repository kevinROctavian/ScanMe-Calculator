package com.kevinroctavian.scanme.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.kevinroctavian.scanme.data.model.ScanResult
import java.net.URLEncoder

/**
 * Screens used in [ScanMeApp]
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AnalyzeImage : Screen("analyze_image")
    object ScanResultDetail : Screen("scan_result_detail")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}

@Composable
fun rememberScanMeAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(navController, context) {
    ScanMeAppState(navController, context)
}

class ScanMeAppState(
    val navController: NavHostController,
    private val context: Context
) {
    fun navigateToScanResult(scanResult: ScanResult) {
        val scanResultString = Gson().toJsonTree(scanResult).asJsonObject.toString()
        navController.navigate(Screen.ScanResultDetail.withArgs(URLEncoder.encode(scanResultString, "UTF-8")))
    }

    fun navigateToAnalyzeImage(args: String) {
        navController.navigate(Screen.AnalyzeImage.withArgs(URLEncoder.encode(args, "UTF-8")))
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}