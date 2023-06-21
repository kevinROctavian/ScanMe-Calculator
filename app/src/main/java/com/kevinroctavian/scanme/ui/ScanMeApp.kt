package com.kevinroctavian.scanme.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.GsonBuilder
import com.kevinroctavian.scanme.data.model.ScanResult
import com.kevinroctavian.scanme.ui.analyzeimage.AnalyzeImageScreen
import com.kevinroctavian.scanme.ui.detail.ScanResultDetailScreen
import com.kevinroctavian.scanme.ui.home.HomeScreen
import java.net.URLDecoder


@Composable
fun ScanMeApp(
    appState: ScanMeAppState = rememberScanMeAppState(),
    startDestination: String = Screen.Home.route
) {
    NavHost(appState.navController, startDestination) {
        composable(Screen.Home.route) {
            HomeScreen(navigateToScanResultDetail = { scanResult ->
                appState.navigateToScanResult(scanResult)
            }, navigateToAnalyzingImage = { path ->
                appState.navigateToAnalyzeImage(path)
            })
        }
        composable(
            Screen.AnalyzeImage.route + "/{path}", arguments = listOf(
                navArgument("path") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )) { backEntry ->
            backEntry.arguments?.getString("path")
                ?.let {
                    AnalyzeImageScreen(
                        path = URLDecoder.decode(it, "UTF-8"),
                        onBackPress = appState::navigateBack
                    )
                }
        }
        composable(Screen.ScanResultDetail.route + "/{scanresult}", listOf(
            navArgument("scanresult") {
                type = NavType.StringType
                defaultValue = ""
            }
        )) { backEntry ->
            val scanResultString = URLDecoder.decode(backEntry.arguments?.getString("scanresult"), "UTF-8")
            if (scanResultString != null) {
                val gson = GsonBuilder().create()
                val scanResult = gson.fromJson(scanResultString, ScanResult::class.java)
                if (scanResult.operator.trim().isEmpty()) scanResult.operator = "+"
                ScanResultDetailScreen(
                    scanResult = scanResult,
                    onBackPress = appState::navigateBack
                )
            }
        }
    }
}