package com.cashcluster.collect.data

data class Item(
    val name: String,
    val yearOfFoundation: String? = null, // Görselde optional gibi duruyor
    val collection: String? = null,
    val country: String? = null,
    val categoryName: String, // Hangi kategoriye ait olduğunu belirtmek için
    val imageUris: List<String> = emptyList() // Şimdilik boş liste, daha sonra doldurulacak
) 