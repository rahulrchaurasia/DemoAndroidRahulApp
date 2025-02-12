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
import java.io.IOException

class PDFHandler {

    companion object {
        private const val CACHE_DIR = "pdf_cache"
        private const val MIME_TYPE = "application/pdf"
    }
    // Enhanced handlePDFResult with better URI handling
    fun handlePDFResult(context: Context, uri: Uri): File? {
        return try {
            // Take URI persistence permission
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            val cacheDir = File(context.cacheDir, CACHE_DIR)
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val fileName = "temp_${System.currentTimeMillis()}.pdf"
            val tempFile = File(cacheDir, fileName)

            // Improved stream handling
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    val buffer = ByteArray(4 * 1024) // 4KB buffer
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            } ?: throw IOException("Could not open input stream")

            // Verify file was created and has content
            if (tempFile.exists() && tempFile.length() > 0) {
                tempFile
            } else {
                throw IOException("File creation failed or file is empty")
            }
        } catch (e: Exception) {
            Log.e("PDFHandler", "Error handling PDF: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Cache management functions
    fun clearCache(context: Context) {
        try {
            val cacheDir = File(context.cacheDir, CACHE_DIR)
            if (cacheDir.exists()) {
                cacheDir.listFiles()?.forEach { file ->
                    if (file.isFile) {
                        val deleted = file.delete()
                        Log.d("PDFHandler", "File ${file.name} deleted: $deleted")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("PDFHandler", "Error clearing cache: ${e.message}")
        }
    }

    fun getCacheSize(context: Context): Long {
        return try {
            val cacheDir = File(context.cacheDir, CACHE_DIR)
            if (cacheDir.exists()) {
                cacheDir.walkTopDown()
                    .filter { it.isFile }
                    .map { it.length() }
                    .sum()
            } else 0
        } catch (e: Exception) {
            Log.e("PDFHandler", "Error getting cache size: ${e.message}")
            0
        }
    }


    // Improved PDF picker launch
    fun launchPDFPicker(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = MIME_TYPE
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
        activity.startActivityForResult(intent, requestCode)
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