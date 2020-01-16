package com.example.travelapp.provider

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class InternalStorageProvider(var context: Context) {

    fun saveBitmap(bitmap: Bitmap, imageName: String) {
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileOutputStream?.close()
        }
    }

    fun loadBitmap(imageName: String): Bitmap? {
        var bitmap: Bitmap? = null
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context.openFileInput(imageName)
            bitmap = BitmapFactory.decodeStream(fileInputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
        }

        return bitmap
    }

    fun deleteBitmap(imageName: String) {
        context.deleteFile(imageName)
    }

    fun renameFile(oldFileName: String, newFileName: String) {
        val oldFile = context.getFileStreamPath(oldFileName)
        val newFile = context.getFileStreamPath(newFileName)
        oldFile.renameTo(newFile)
    }
}