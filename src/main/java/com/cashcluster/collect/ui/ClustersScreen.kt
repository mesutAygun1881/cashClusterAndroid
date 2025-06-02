package com.cashcluster.collect.ui
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import com.cashcluster.collect.data.Category
import com.cashcluster.collect.data.CategoryStorage
import kotlinx.coroutines.launch
import com.cashcluster.collect.data.Item
import com.cashcluster.collect.data.ItemStorage

enum class CollectionCategory {
    Coins,
    Banknotes,
    Custom // Kullanıcı tanımlı kategoriler için
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClustersScreen() {
    val context = LocalContext.current
    val categoryStorage = remember { CategoryStorage(context) }
    val itemStorage = remember { ItemStorage(context) }

    // Kategorileri yükle veya varsayılanları oluştur
    var categories by remember {
        val loadedCategories = categoryStorage.loadCategories()
        if (loadedCategories.isEmpty()) {
            val defaultCategories = listOf(
                Category("Coins", listOf("Name", "Year of foundation", "Collection", "Country")),
                Category("Banknotes", listOf("Name", "Year of foundation", "Collection", "Country"))
            )
            categoryStorage.saveCategories(defaultCategories)
            mutableStateOf(defaultCategories)
        } else {
            mutableStateOf(loadedCategories)
        }
    }

    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }
    var showNewCategorySheet by remember { mutableStateOf(false) }
    var showNewItemSheet by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    // Itemları yükle
    var items by remember { mutableStateOf(itemStorage.loadItems()) }

    // Varsayılan kategoriler için enum yerine Category objesi kullanıyoruz.
    // Bu kısım UI güncellemesi için tutulabilir, veya doğrudan categories listesi kullanılabilir.
    // Şimdilik selectedCategory Category tipinde tutulacak.

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Cluster",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Kategori butonları - LazyRow ile yatay kaydırılabilir ve FAB sonda
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    val backgroundColor = if (isSelected) Color(0xFF1D3D98) else Color.Transparent
                    val contentColor = if (isSelected) Color.White else Color(0xFF1D3D98)

                    OutlinedButton(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF1D3D98)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = backgroundColor,
                            contentColor = contentColor
                        )
                    ) {
                        Text(category.name)
                    }
                }

                item {
                    FloatingActionButton(
                        onClick = {
                            coroutineScope.launch { sheetState.show() }
                            showNewCategorySheet = true
                        },
                        modifier = Modifier.size(40.dp),
                        containerColor = Color(0xFF1D3D98)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            }

            // Ortadaki boş durum göstergesi veya kategori içeriği
            if (selectedCategory == null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .border(2.dp, Color.Gray, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("X", fontSize = 40.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "There are no items in this section of your collection yet. They will appear here when you add them.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                }
            } else {
                // Seçili kategori içeriği
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 16.dp)
                ) {
                    val currentCategory = selectedCategory
                    if (currentCategory != null) {
                        Text(
                            "${currentCategory.name} Items",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Seçili kategoriye ait itemları filtrele ve listele
                        val categoryItems = items.filter { it.categoryName == currentCategory.name }

                        if (categoryItems.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // Büyük X ikonu
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .border(2.dp, Color.Gray, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("X", fontSize = 40.sp, color = Color.Gray)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "There are no items in this section of your collection yet. They will appear here when you add them.",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                )
                            }
                        }else {
                            LazyColumn {
                                items(categoryItems.chunked(2)) { rowItems ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        rowItems.forEach { item ->
                                            Card(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .aspectRatio(1f)
                                                    .clickable { selectedItem = item },
                                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                ) {
                                                    if (item.imageUris.isNotEmpty()) {
                                                        val imagePainter = rememberAsyncImagePainter("file://${item.imageUris.first()}")
                                                        Image(
                                                            painter = imagePainter,
                                                            contentDescription = item.name,
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .clip(RoundedCornerShape(8.dp)),
                                                            contentScale = ContentScale.Crop
                                                        )
                                                    }

                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .align(Alignment.BottomStart)
                                                            .background(Color(0xAA000000))
                                                            .padding(8.dp)
                                                    ) {
                                                        Text(
                                                            text = item.name,
                                                            color = Color.White,
                                                            style = MaterialTheme.typography.bodyMedium
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        if (rowItems.size < 2) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                if (selectedCategory != null) {
                    showNewItemSheet = true
                } else {
                    // Kategori seçili değilse uyarı gösterilebilir veya buton disable edilebilir.
                    // Şimdilik hiçbir şey yapmıyoruz.
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
            shape = RoundedCornerShape(24.dp),
            enabled = selectedCategory != null
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Filled.Add, contentDescription = "Add new item", tint = Color.White)
                Text("Add new item", color = Color.White)
            }
        }
    }

    // Item Detay Sheet
    if (selectedItem != null) {
        ItemDetailSheet(
            item = selectedItem!!,
            onDismiss = { selectedItem = null },
            onNameUpdate = { newName ->
                items = items.map { if (it == selectedItem) it.copy(name = newName) else it }
                itemStorage.saveItems(items)
                selectedItem = selectedItem?.copy(name = newName)
            },
            onDelete = {
                items = items.filter { it != selectedItem }
                itemStorage.saveItems(items)
                selectedItem = null // Sheet'i kapat
            }
        )
    }

    // Yeni Kategori Sheet
    if (showNewCategorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showNewCategorySheet = false },
            sheetState = sheetState
        ) {
            NewCategorySheet(
                onDismiss = {
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) { showNewCategorySheet = false }
                    }
                },
                onCategoryCreated = { newCategory ->
                    categories = categories + newCategory
                    categoryStorage.saveCategories(categories)
                    selectedCategory = newCategory
                    coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) { showNewCategorySheet = false }
                    }
                }
            )
        }
    }

    // Yeni Item Sheet
    if (showNewItemSheet && selectedCategory != null) {
        val currentCategory = selectedCategory
        ModalBottomSheet(
            onDismissRequest = { showNewItemSheet = false },
            sheetState = sheetState
        ) {
            NewItemSheet(
                categoryName = currentCategory!!.name,
                categoryFields = currentCategory.fields,
                onDismiss = { showNewItemSheet = false },
                onItemCreated = { newItem ->
                    items = items + newItem
                    itemStorage.saveItems(items)
                    showNewItemSheet = false
                }
            )
        }
        LaunchedEffect(showNewItemSheet) {
            if (showNewItemSheet) {
                sheetState.expand()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewClustersScreen() {
    ClustersScreen()
}