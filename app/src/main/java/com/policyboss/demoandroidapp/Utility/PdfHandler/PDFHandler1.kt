package com.policyboss.demoandroidapp.Utility.PdfHandler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class PDFHandler1 {

    companion object {
        private const val CACHE_DIR = "pdf_cache"
        private const val MIME_TYPE = "application/pdf"
    }

    // Launch PDF picker
    fun launchPDFPicker(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = MIME_TYPE
        }
        activity.startActivityForResult(intent, requestCode)
    }

    // Handle selected PDF
    fun handlePDFResult(context: Context, uri: Uri): File? {
        return try {
            // Create cache directory if it doesn't exist
            val cacheDir = File(context.cacheDir, CACHE_DIR)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            // Create temporary file in cache
            val fileName = "temp_${System.currentTimeMillis()}.pdf"
            val tempFile = File(cacheDir, fileName)

            // Copy PDF content to cache
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun verifyPDFCache(context: Context, cachedFile: File?) {
        if (cachedFile != null) {
            // Check if file exists in cache
            val exists = cachedFile.exists()
            // Get file size
            val fileSize = cachedFile.length()
            // Get cache directory path
            val cachePath = cachedFile.absolutePath

            Log.d("PDFCache", "File exists: $exists")
            Log.d("PDFCache", "File size: $fileSize bytes")
            Log.d("PDFCache", "Cache path: $cachePath")
        }
    }

    // Upload PDF to server
    suspend fun uploadPDFToServer(file: File, serverUrl: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Create multipart request body
                val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("pdf", file.name, requestFile)

                // Create retrofit service and make API call
                //val response = RetrofitClient.apiService.uploadPDF(body)
                //response.isSuccessful
                return@withContext true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}