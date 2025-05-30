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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun OnboardingPage3(onContinue: () -> Unit, onSkip: () -> Unit, onBack: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val whiteAreaHeight = screenHeight * 0.35f
    val scrollState = rememberScrollState()

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
                .padding(horizontal = 24.dp, vertical = 10.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 10.dp)
                .padding(top = 30.dp)
            ,
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
                painter = painterResource(id = R.drawable.image3),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(9f / 18f)
                    .clip(RoundedCornerShape(28.dp))
            )

            // Başlık ve açıklama hizalı
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Photo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Add new exhibits and build to your collections",
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }

            // Butonlar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp), // Yan ve alt boşluk
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Butonlar arasına boşluk
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)) // Kırmızı arka plan
                ) {
                    Text("Continue", color = Color.White)
                }

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    Text("Skip onboarding", color = Color.White)
                }
            }
        }
    }
}