package com.cashcluster.collect.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List // Clusters ikonu için
import androidx.compose.material.icons.filled.Settings // Filter ikonu için

// Sekme bilgileri için veri sınıfı
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val items = listOf(
        BottomNavItem("Clusters", Icons.Filled.List, { ClustersScreen() }), // Clusters sekmesi
        BottomNavItem("Filter", Icons.Filled.Settings, { FilterScreen() }) // Filter sekmesi
    )
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Red, unselectedIconColor = Color.Red, selectedTextColor = Color.Red, unselectedTextColor = Color.Red)
                    )
                }
            }
        }
    ) { innerPadding ->
        // Seçili sekmeye göre içeriği göster
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            items[selectedItem].screen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}