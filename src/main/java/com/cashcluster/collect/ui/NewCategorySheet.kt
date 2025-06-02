package com.cashcluster.collect.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.data.Category
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCategorySheet(
    onDismiss: () -> Unit,
    onCategoryCreated: (Category) -> Unit
) {
    var categoryName by remember { mutableStateOf<String>("") }
    val defaultFields = remember {
        mutableStateListOf<String>().apply {
            add("Name")
            add("Year of foundation")
        }
    }
    val customFields = remember { mutableStateListOf<String>() }
    var newFieldName by remember { mutableStateOf<String>("") }
    var showNewFieldInput by remember { mutableStateOf<Boolean>(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "New Category",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            Text(
                text = "Default Fields",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D3D98)
            )
        }

        items(defaultFields) { field ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Text(
                    text = field,
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }
        }

        item {
            Text(
                text = "Custom Fields",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1D3D98),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(customFields) { field ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = field)
                    IconButton(
                        onClick = { customFields.remove(field) }
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete field",
                            tint = Color.Red
                        )
                    }
                }
            }
        }

        if (showNewFieldInput) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = newFieldName,
                        onValueChange = { newFieldName = it },
                        label = { Text("New Field Name") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    IconButton(
                        onClick = {
                            if (newFieldName.isNotBlank()) {
                                customFields.add(newFieldName)
                                newFieldName = ""
                                showNewFieldInput = false
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add field",
                            tint = Color(0xFF1D3D98)
                        )
                    }
                }
            }
        }

        item {
            TextButton(
                onClick = { showNewFieldInput = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add new field")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add new field", color = Color(0xFF1D3D98))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1D3D98))
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        if (categoryName.isNotBlank()) {
                            val lowerCaseCategory = categoryName.trim().lowercase()
                            val combinedFields = if (lowerCaseCategory == "coins" || lowerCaseCategory == "banknotes") {
                                listOf("Name", "Year of foundation", "Collection", "Country")
                            } else {
                                defaultFields.toList() + customFields.toList()
                            }
                            val newCategory = Category(
                                categoryName,
                                combinedFields
                            )
                            onCategoryCreated(newCategory)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D3D98))
                ) {
                    Text("Create Category", color = Color.White)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}