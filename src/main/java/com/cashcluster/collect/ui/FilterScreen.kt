package com.cashcluster.collect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.data.Category
import com.cashcluster.collect.data.CategoryStorage
import com.cashcluster.collect.data.Item
import com.cashcluster.collect.data.ItemStorage
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen() {
    val context = LocalContext.current
    val categoryStorage = remember { CategoryStorage(context) }
    val itemStorage = remember { ItemStorage(context) }
    val categories = remember { categoryStorage.loadCategories() }
    val items = remember { itemStorage.loadItems() }

    var expandedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedCategories by remember { mutableStateOf(setOf<String>()) }
    var selectedParams by remember { mutableStateOf(mutableMapOf<String, MutableSet<String>>()) }

    // Filtrelenmiş sonuçlar için state
    var filteredItems by remember { mutableStateOf<List<Item>?>(null) }
    var showResultSheet by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Item?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Choose filter parameters",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Text(
                    text = "Collection:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(categories) { category ->
                val isExpanded = expandedCategories.contains(category.name)
                val isChecked = selectedCategories.contains(category.name)
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {
                                if (it) {
                                    selectedCategories = selectedCategories + category.name
                                    expandedCategories = expandedCategories + category.name
                                } else {
                                    selectedCategories = selectedCategories - category.name
                                    expandedCategories = expandedCategories - category.name
                                }
                            }
                        )
                        Text(
                            text = category.name,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    if (isChecked) {
                                        selectedCategories = selectedCategories - category.name
                                        expandedCategories = expandedCategories - category.name
                                    } else {
                                        selectedCategories = selectedCategories + category.name
                                        expandedCategories = expandedCategories + category.name
                                    }
                                }
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = null
                        )
                    }
                    // Alt parametreler
                    if (isExpanded) {
                        val categoryItems = items.filter { it.categoryName == category.name }
                        val paramMap = buildMap<String, Set<String>> {
                            category.fields.forEach { field ->
                                val values = categoryItems.mapNotNull { item ->
                                    when (field) {
                                        "Name" -> item.name
                                        "Year of foundation" -> item.yearOfFoundation
                                        "Collection" -> item.collection
                                        "Country" -> item.country
                                        else -> item.customFields[field]
                                    }
                                }.filter { !it.isNullOrBlank() }.toSet()
                                if (values.isNotEmpty()) put(field, values)
                            }
                        }
                        paramMap.forEach { (param, values) ->
                            Text(
                                text = "$param:",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(start = 32.dp, top = 8.dp)
                            )
                            values.forEach { value ->
                                val checked = selectedParams[category.name]?.contains(value) == true
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val newMap = selectedParams.toMutableMap()
                                            val oldSet = newMap[category.name]?.toMutableSet() ?: mutableSetOf()
                                            if (checked) oldSet.remove(value) else oldSet.add(value)
                                            newMap[category.name] = oldSet
                                            selectedParams = newMap
                                        }
                                        .padding(start = 48.dp, top = 4.dp, bottom = 4.dp)
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = {
                                            val newMap = selectedParams.toMutableMap()
                                            val oldSet = newMap[category.name]?.toMutableSet() ?: mutableSetOf()
                                            if (it) oldSet.add(value) else oldSet.remove(value)
                                            newMap[category.name] = oldSet
                                            selectedParams = newMap
                                        }
                                    )
                                    Text(text = value)
                                }
                            }
                        }
                    }
                }
                Divider()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {
                selectedCategories = emptySet()
                selectedParams = mutableMapOf()
            }) {
                Text("Reset all filters")
            }
            Button(
                onClick = {
                    // Filtrele ve sheet aç
                    val filtered = items.filter { item ->
                        selectedCategories.contains(item.categoryName) &&
                        (selectedParams[item.categoryName]?.all { paramValue ->
                            paramValue in listOfNotNull(
                                item.name,
                                item.yearOfFoundation,
                                item.collection,
                                item.country
                            ) + (item.customFields.values)
                        } ?: true)
                    }
                    filteredItems = filtered
                    showResultSheet = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D3D98))
            ) {
                Text("Apply filters", color = Color.White)
            }
        }
    }

    // Sonuçları gösteren sheet
    if (showResultSheet && filteredItems != null) {
        ModalBottomSheet(
            onDismissRequest = { showResultSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filtered Results",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                if (filteredItems!!.isEmpty()) {
                    Text("No items found.")
                } else {
                    // ClusterScreen'deki gibi grid
                    LazyColumn {
                        items(filteredItems!!.chunked(2)) { rowItems ->
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
                                            modifier = Modifier.fillMaxSize()
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

    // İstersen item detay sheet'i de açabilirsin:
    if (selectedItem != null) {
        ItemDetailSheet(
            item = selectedItem!!,
            onDismiss = { selectedItem = null },
            onNameUpdate = { newName ->
                // Hem items hem filteredItems güncellenmeli
                filteredItems = filteredItems?.map { if (it == selectedItem) it.copy(name = newName) else it }
                val newItems = items.map { if (it == selectedItem) it.copy(name = newName) else it }
                itemStorage.saveItems(newItems)
                selectedItem = selectedItem?.copy(name = newName)
            },
            onDelete = {
                // Silme işlemi: hem items hem filteredItems listesinden çıkar
                filteredItems = filteredItems?.filter { it != selectedItem }
                val newItems = items.filter { it != selectedItem }
                itemStorage.saveItems(newItems)
                selectedItem = null
            }
        )
    }
} 