package com.cashcluster.collect.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.R

@Composable
fun OnboardingPage4(onContinue: () -> Unit, onSkip: () -> Unit, onBack: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val whiteAreaHeight = screenHeight * 0.35f

    Box(modifier = Modifier.fillMaxSize()) {
        // Üst beyaz alan
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(whiteAreaHeight)
                .background(Color.White)
        )

        // Alt mavi arka plan
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = whiteAreaHeight)
                .background(color = Color(0xFF1D3D98))
        )

        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Geri butonu
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Back", color = Color.Black)
            }

            // Görsel (mockup)
            Image(
                painter = painterResource(id = R.drawable.image4),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(9f / 18f)
                    .clip(RoundedCornerShape(28.dp))
            )

            // Başlık ve açıklama
            Column(
                modifier = Modifier
                   .fillMaxWidth()
                   .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Filters",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Filter exhibits by specific parameters",
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }

            // Butonlar
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Start collect!", color = Color.White)
                }

                TextButton(onClick = onSkip) {
                    Text("Skip onboarding", color = Color.White)
                }
            }
        }
    }
}