package com.kevinroctavian.scanme.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinroctavian.scanme.ui.theme.ScanMeTheme
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenKtTest{

    @get: Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        composeTestRule.setContent {    // setting our composable as content for test
            ScanMeTheme {
                HomeScreen(navigateToScanResultDetail = {}, navigateToAnalyzingImage = {})
            }
        }
    }

    @Test
    fun verify_if_all_views_exists() {
        composeTestRule.onNodeWithTag("List Scan Results").assertExists()
        composeTestRule.onNodeWithTag("Button Add Input").assertExists()
        composeTestRule.onNodeWithText("Use File Storage").assertExists()
        composeTestRule.onNodeWithText("Use Database Storage").assertExists()
    }
}