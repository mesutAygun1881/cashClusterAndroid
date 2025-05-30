package com.cashcluster.collect.ui
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.R
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState

@Composable
fun OnboardingPage2(onContinue: () -> Unit, onSkip: () -> Unit, onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        // Üst beyaz zemin
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(Color.White)
        )

        // Alt mavi zemin
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = (0.35f * LocalConfiguration.current.screenHeightDp).dp)
                .background(color = Color(0xFF1D3D98))
        )

        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 10.dp)
                .verticalScroll(scrollState)
                .padding(bottom = 5.dp)
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Back button
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Back", color = Color.Black)
            }

            // Mockup image - üst beyaz kısmın ortasında
            Image(
                painter = painterResource(id = R.drawable.image2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(9f / 18f)
                    .clip(RoundedCornerShape(28.dp))
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Cluster",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Text(
                    text = "Keep track of your collections and see exhibit information",
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }

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