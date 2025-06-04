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
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import android.util.Log
import android.widget.Toast
import android.net.Uri
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
    var selectedParams by remember { mutableStateOf(mutableMapOf<Pair<String, String>, MutableSet<String>>()) }

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
                                val checked = selectedParams[category.name to param]?.contains(value) == true
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val newMap = selectedParams.toMutableMap()
                                            val key = category.name to param
                                            val oldSet = newMap[key]?.toMutableSet() ?: mutableSetOf()
                                            if (checked) oldSet.remove(value) else oldSet.add(value)
                                            newMap[key] = oldSet
                                            selectedParams = newMap
                                        }
                                        .padding(start = 48.dp, top = 4.dp, bottom = 4.dp)
                                ) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = {
                                            val newMap = selectedParams.toMutableMap()
                                            val key = category.name to param
                                            val oldSet = newMap[key]?.toMutableSet() ?: mutableSetOf()
                                            if (it) oldSet.add(value) else oldSet.remove(value)
                                            newMap[key] = oldSet
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
                    try {
                        Log.d("FilterScreen", "Filter started")
                        val filtered = items.filter { item ->
                            Log.d("FilterScreen", "Filtering item: ${item.name}")
                            val categoryParams = selectedParams.filterKeys { it.first == item.categoryName }
                            Log.d("FilterScreen", "CategoryParams: $categoryParams")
                            val result = selectedCategories.contains(item.categoryName) &&
                                (
                                    categoryParams.isEmpty() ||
                                    categoryParams.all { (key, values) ->
                                        val (_, field) = key
                                        val itemValue = when (field) {
                                            "Name" -> item.name
                                            "Year of foundation" -> item.yearOfFoundation
                                            "Collection" -> item.collection
                                            "Country" -> item.country
                                            else -> item.customFields[field]
                                        }
                                        values.isEmpty() || values.contains(itemValue)
                                    }
                                )
                            Log.d("FilterScreen", "Result for item ${item.name}: $result")
                            result
                        }
                        filteredItems = filtered
                        showResultSheet = true
                        Log.d("FilterScreen", "Filter finished, found: ${filtered.size}")
                    } catch (e: Exception) {
                        Log.e("FilterScreen", "Filter crash: ${e.message}", e)
                        Toast.makeText(context, "Filter error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                    ) {
                        items(filteredItems!!) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { selectedItem = item },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    if (item.imageUris.isNotEmpty()) {
                                        val imagePainter = rememberAsyncImagePainter(model = Uri.parse(item.imageUris.first()))
                                        Image(
                                            painter = imagePainter,
                                            contentDescription = item.name,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = item.name,
                                            color = Color.Black,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        // Show more info if needed
                                    }
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