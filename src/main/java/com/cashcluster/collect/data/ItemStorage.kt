package com.cashcluster.collect.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ItemStorage(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("item_storage", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val ITEMS_KEY = "items"

    fun saveItems(items: List<Item>) {
        val json = gson.toJson(items)
        sharedPreferences.edit().putString(ITEMS_KEY, json).apply()
    }

    fun loadItems(): List<Item> {
        val json = sharedPreferences.getString(ITEMS_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Item>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
} 