package com.cashcluster.collect.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.R
import androidx.compose.ui.graphics.toArgb

@Composable
fun OnboardingPage1(onContinue: () -> Unit, onSkip: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF1D3D98)) // Arka plan rengi
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // İçeriği dikeyde yay
        ) {
            // Üst Kısım: Başlık
            Text(
                text = "Welcome to the Cash Cluster!", // Metin
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 64.dp, bottom = 16.dp) // Üst ve alt boşluk
            )

            // Orta Kısım: Resim
            Image(
                painter = painterResource(id = R.drawable.image1), // image1 kullanılacak
                contentDescription = null,
                contentScale = ContentScale.Crop, // Resmi tam sığdır olarak geri alındı
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Kalan dikey alanı doldur olarak geri alındı
                    // .padding(vertical = 16.dp) // Dikey boşluk kaldırıldı
            )

            // Alt Kısım: Butonlar
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
                    Text(text = stringResource(id = R.string.onboarding_continue), color = Color.White) // Yazı rengi beyaz
                }
                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.skip), color = Color.White) // Yazı rengi beyaz
                }
            }
        }
    }
} 