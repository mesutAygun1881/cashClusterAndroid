package com.cashcluster.collect.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var currentPage by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1D3D98)) // Arka plan rengi eklendi
    ) {
        when (currentPage) {
            0 -> OnboardingPage1(
                onContinue = { currentPage++ },
                onSkip = onFinish
            )
            1 -> OnboardingPage2(
                onContinue = { currentPage++ },
                onSkip = onFinish,
                onBack = { currentPage-- }
            )
            2 -> OnboardingPage3(
                onContinue = { currentPage++ },
                onBack = {currentPage--},
                onSkip = onFinish
            )
            3 -> OnboardingPage4(
                onContinue = onFinish,
                onBack = {currentPage--},
                onSkip = onFinish
            )
        }
    }
} 