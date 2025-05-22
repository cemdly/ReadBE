package ru.startandroid.develop.readbe20.parser

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
//
fun copyFileToInternalStorage(context: Context, uri: Uri) {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = getFileName(context, uri) ?: "book.epub"
        val file = File(context.filesDir, fileName)

        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        println("Файл сохранен: ${file.absolutePath}")
    } catch (e: Exception) {
        e.printStackTrace()
        println("Ошибка при копировании файла: ${e.message}")
    }
}

@SuppressLint("Range")
fun getFileName(context: Context, uri: Uri): String? {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
}