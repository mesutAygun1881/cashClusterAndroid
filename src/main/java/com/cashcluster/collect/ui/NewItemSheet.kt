package com.cashcluster.collect.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.data.Item // Item data class'ını import ediyoruz
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewItemSheet(
    categoryName: String, // Hangi kategoriye item eklendiğini bilmek için
    onDismiss: () -> Unit,
    onItemCreated: (Item) -> Unit,
    categoryFields: List<String>
) {
    // Dynamic field states
    val fieldStates = remember {
        categoryFields.associateWith { mutableStateOf("") }.toMutableMap()
    }
    var imageUris by remember { mutableStateOf(emptyList<String>()) } // Şimdilik string listesi

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (imageUris.size < 3) {
                val inputStream = context.contentResolver.openInputStream(it)
                inputStream?.use { stream ->
                    val fileName = "${UUID.randomUUID()}.jpg"
                    val file = File(context.filesDir, fileName)
                    val outputStream = FileOutputStream(file)
                    stream.copyTo(outputStream)
                    outputStream.close()
                    imageUris = imageUris + file.absolutePath
                }
            }
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            Text(
                text = "New item",
                fontSize = 24.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Görsel ekleme placeholderları
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(color = androidx.compose.ui.graphics.Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .border(1.dp, androidx.compose.ui.graphics.Color.Gray, RoundedCornerShape(8.dp))
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (index < imageUris.size) {
                            val imageUri = imageUris[index]
                            val painter = rememberAsyncImagePainter(model = "file://$imageUri")
                            Image(
                                painter = painter,
                                contentDescription = "Selected image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        } else {
                            Icon(Icons.Default.Add, contentDescription = "Add image", tint = androidx.compose.ui.graphics.Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Collection elements for items",
                fontSize = 18.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Dynamic text fields based on categoryFields
            categoryFields.forEach { field ->
                val state = fieldStates[field]!!
                OutlinedTextField(
                    value = state.value,
                    onValueChange = { state.value = it },
                    label = { Text(field) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Add new item butonu
            val nameInput = fieldStates["Name"]?.value.orEmpty()
            Button(
                onClick = {
                    if (nameInput.isNotBlank()) {
                        val customFields = fieldStates.filterKeys {
                            it != "Name" && it != "Year of foundation" && it != "Collection" && it != "Country"
                        }.mapValues { it.value.value }
                        val newItem = Item(
                            name = nameInput,
                            yearOfFoundation = fieldStates["Year of foundation"]?.value?.ifBlank { null },
                            collection = fieldStates["Collection"]?.value?.ifBlank { null },
                            country = fieldStates["Country"]?.value?.ifBlank { null },
                            categoryName = categoryName,
                            imageUris = imageUris,
                            customFields = customFields
                        )
                        onItemCreated(newItem)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFD3E0F1) // Açık mavi gri tonu
                ),
                enabled = nameInput.isNotBlank() // İsim boş değilse aktif
            ) {
                Text("Add new item", color = androidx.compose.ui.graphics.Color(0xFF1D3D98)) // Koyu mavi yazı
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Cancel, back butonu
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cancel, back", color = androidx.compose.ui.graphics.Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

