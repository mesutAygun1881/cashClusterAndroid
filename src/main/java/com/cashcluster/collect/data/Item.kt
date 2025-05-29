package com.cashcluster.collect.data

data class Item(
    val name: String,
    val yearOfFoundation: String? = null,
    val collection: String? = null,
    val country: String? = null,
    val categoryName: String,
    val imageUris: List<String> = emptyList(),
    val customFields: Map<String, String> = emptyMap()
)
