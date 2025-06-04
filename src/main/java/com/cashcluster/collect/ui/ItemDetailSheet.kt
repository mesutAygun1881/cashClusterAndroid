package com.cashcluster.collect.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashcluster.collect.data.ImageStorage
import com.cashcluster.collect.data.Item
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import android.net.Uri
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailSheet(
    item: Item,
    onDismiss: () -> Unit,
    onNameUpdate: (String) -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val imageStorage = remember { ImageStorage(context) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isEditingName by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(item.name) }

    LaunchedEffect(Unit) { sheetState.expand() }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            // Name alanı ve edit butonu
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isEditingName) {
                    TextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            onNameUpdate(editedName)
                            isEditingName = false
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("Save")
                    }
                } else {
                    Text(
                        text = item.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = { isEditingName = true },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF1D3D98), shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }
            }

            // Resimler
            if (item.imageUris.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(item.imageUris) { imagePath ->
                        val painter = rememberAsyncImagePainter(model = Uri.parse(imagePath))
                        Image(
                            painter = painter,
                            contentDescription = "Item image",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Detaylar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DetailRow("Name", item.name)
                    DetailRow("Year of foundation", item.yearOfFoundation)
                    DetailRow("Country", item.country)
                    DetailRow("Collection", item.collection)

                    // Custom fields
                    item.customFields.forEach { (label, value) ->
                        DetailRow(label, value)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Delete this item",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { showDeleteDialog = true }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Back butonu
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D3D98)
                )
            ) {
                Text("Back", color = Color.White, fontSize = 20.sp)
            }
        }

        // Delete onay popup'ı
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Item") },
                text = { Text("Are you sure you want to delete this item?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onDelete()
                        }
                    ) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false }
                    ) { Text("No") }
                }
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String?) {
    if (value != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
} 