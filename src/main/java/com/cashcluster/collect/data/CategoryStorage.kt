package com.cashcluster.collect.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryStorage(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("category_storage", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val CATEGORIES_KEY = "categories"

    fun saveCategories(categories: List<Category>) {
        val json = gson.toJson(categories)
        sharedPreferences.edit().putString(CATEGORIES_KEY, json).apply()
    }

    fun loadCategories(): List<Category> {
        val json = sharedPreferences.getString(CATEGORIES_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Category>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}