

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.policyboss.demoandroidapp.Utility.PdfHandler.PDFHandler.Companion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PDFHandlerAdv {
    companion object {
        private const val CACHE_DIR = "pdf_cache"
        private const val MIME_TYPE = "application/pdf"
    }

    // Enhanced handlePDFResult with RealPath utility
    fun handlePDFResult(context: Context, uri: Uri): File? {
        return try {
            // First try to get real path
            val realPath = getRealPathFromUri(context, uri)

            if (realPath != null) {
                // If we have real path, use direct file copy
                val sourceFile = File(realPath)
                if (sourceFile.exists()) {
                    copyToCache(context, sourceFile)
                } else {
                    // Fallback to stream copy if file doesn't exist
                    copyFromUriToCache(context, uri)
                }
            } else {
                // Fallback to stream copy if real path not available
                copyFromUriToCache(context, uri)
            }
        } catch (e: Exception) {
            Log.e("PDFHandler", "Error handling PDF: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    // Utility to get real file path from URI
    private fun getRealPathFromUri(context: Context, uri: Uri): String? {
        // Handle different URI schemes
        when {
            // File URI scheme
            uri.scheme == "file" -> {
                return uri.path
            }

            // Content URI scheme
            uri.scheme == "content" -> {
                val projection = arrayOf(MediaStore.MediaColumns.DATA)
                context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                        return cursor.getString(columnIndex)
                    }
                }

                // For downloaded files
                if (uri.toString().contains("downloads")) {
                    val id = DocumentsContract.getDocumentId(uri)
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "")
                        }
                        try {
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                id.toLong()
                            )
                            return getDataColumn(context, contentUri, null, null)
                        } catch (e: NumberFormatException) {
                            Log.e("PDFHandler", "Download URI parsing failed", e)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    // Copy file directly from source
    private fun copyToCache(context: Context, sourceFile: File): File? {
        val cacheDir = File(context.cacheDir, CACHE_DIR).apply { mkdirs() }
        val destinationFile = File(cacheDir, "temp_${System.currentTimeMillis()}.pdf")

        return try {
            sourceFile.inputStream().use { input ->
                destinationFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            if (destinationFile.exists() && destinationFile.length() > 0) {
                destinationFile
            } else null
        } catch (e: Exception) {
            Log.e("PDFHandler", "Error copying file: ${e.message}")
            null
        }
    }

    // Copy file from URI using streams
    private fun copyFromUriToCache(context: Context, uri: Uri): File? {
        val cacheDir = File(context.cacheDir, CACHE_DIR).apply { mkdirs() }
        val destinationFile = File(cacheDir, "temp_${System.currentTimeMillis()}.pdf")

        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    val buffer = ByteArray(8 * 1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                    output.flush()
                }
            }
            if (destinationFile.exists() && destinationFile.length() > 0) {
                destinationFile
            } else null
        } catch (e: Exception) {
            Log.e("PDFHandler", "Error copying from URI: ${e.message}")
            null
        }
    }

    // Launch PDF picker with all possible locations
    fun launchPDFPicker(activity: Activity, requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = MIME_TYPE
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            // Allow multiple storage locations
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            // Include downloads
            putExtra("android.provider.extra.SHOW_ADVANCED", true)
        }

        // Create chooser to show all possible locations
        val chooserIntent = Intent.createChooser(intent, "Select PDF from Document Folder only")
        activity.startActivityForResult(chooserIntent, requestCode)
    }


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


    // Cache management functions
    fun clearCache(context: Context) {
        try {
            val cacheDir = File(context.cacheDir, PDFHandlerAdv.CACHE_DIR)
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
            val cacheDir = File(context.cacheDir, PDFHandlerAdv.CACHE_DIR)
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
}