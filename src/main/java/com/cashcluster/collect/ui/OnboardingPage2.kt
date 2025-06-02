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

@Composable
fun OnboardingPage2(onContinue: () -> Unit, onSkip: () -> Unit, onBack: () -> Unit) {
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
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp), // Alt butonlar için padding

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Back button (her zaman üstte ve görünür)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 30.dp)
                    .padding(top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("Back", color = Color.Black)
                }
            }

            // Mockup image
            Image(
                painter = painterResource(id = R.drawable.image2),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(9f / 18f)
                    .clip(RoundedCornerShape(28.dp))
            )

            // Cluster başlığı ve açıklama - sola yaslı
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "Cluster",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Keep track of your collections and see exhibit information",
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Butonlar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp),
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
                    Text("Continue", color = Color.White)
                }

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Skip onboarding", color = Color.White)
                }
            }
        }
    }
}