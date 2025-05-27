package com.cashcluster.collect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClustersScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Arka plan beyaz
            .padding(16.dp) // Genel boşluk
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Başlık
            Text(
                text = "Cluster",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Buton Sırası (Coins, Banknotes, +)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Butonlar arası boşluk
            ) {
                Button(
                    onClick = { /* Coins tıklama */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D3D98)), // Mavi arka plan
                    shape = RoundedCornerShape(8.dp) // Yuvarlak köşeler
                ) {
                    Text("Coins", color = Color.White)
                }
                OutlinedButton(
                    onClick = { /* Banknotes tıklama */ },
                    modifier = Modifier.border(1.dp, Color(0xFF1D3D98), shape = RoundedCornerShape(8.dp)), // Mavi border
                    shape = RoundedCornerShape(8.dp) // Yuvarlak köşeler
                ) {
                    Text("Banknotes", color = Color(0xFF1D3D98)) // Mavi yazı
                }
                Spacer(modifier = Modifier.weight(1f)) // Artı butonunu sağa iter
                FloatingActionButton(
                    onClick = { /* Artı tıklama */ },
                    modifier = Modifier.size(40.dp), // Boyut ayarı
                    containerColor = Color(0xFF1D3D98) // Mavi arka plan
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White) // Artı ikonu
                }
            }

            // Boş Durum Göstergesi (Ortalanmış)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Kalan dikey alanı kapla ve ortala
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Dikeyde ortala
            ) {
                // Yuvarlak Çarpı İkonu Placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp) // Boyut
                        .border(2.dp, Color.Gray, CircleShape), // Gri border
                    contentAlignment = Alignment.Center
                ) {
                    Text("X", fontSize = 40.sp, color = Color.Gray) // Çarpı işareti placeholder
                }
                Spacer(modifier = Modifier.height(16.dp)) // İkon ile metin arası boşluk
                Text(
                    text = "There are no items in this section of your collection yet. They will appear here when you add them.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp) // Metin için yatay boşluk
                )
            }
            // Alt Buton (Sağ altta - Ana Box içinde hizalayacağız)
        }
         // Sağ alttaki kırmızı buton
        Button(
            onClick = { /* Add New Item tıklama */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), // Sağ ve alttan boşluk
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)), // Kırmızı arka plan
            shape = RoundedCornerShape(24.dp) // Daha yuvarlak köşeler
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                 Icon(Icons.Filled.Add, contentDescription = "Add new item", tint = Color.White)
                 Text("Add new item", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClustersScreen() {
    ClustersScreen()
} 