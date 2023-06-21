package com.kevinroctavian.scanme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kevinroctavian.scanme.ui.ScanMeApp
import com.kevinroctavian.scanme.ui.theme.ScanMeTheme
//import org.opencv.android.OpenCVLoader

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        OpenCVLoader.initDebug()
        setContent {
            ScanMeTheme {
                ScanMeApp()
            }
        }
    }
}