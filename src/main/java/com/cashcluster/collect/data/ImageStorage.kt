package com.cashcluster.collect.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ImageStorage(private val context: Context) {
    private val imagesDir: File
        get() = File(context.filesDir, "item_images").apply { mkdirs() }

    fun saveImage(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        
        val fileName = "${UUID.randomUUID()}.jpg"
        val file = File(imagesDir, fileName)
        
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        
        return file.absolutePath
    }

    fun loadImage(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            null
        }
    }

    fun deleteImage(path: String) {
        try {
            File(path).delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 