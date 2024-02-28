package com.policyboss.demoandroidapp.Utility

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Base64.encodeToString
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.policyboss.demoandroidapp.BuildConfig
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.io.FileDescriptor
import java.io.File

object Utility {


    fun uriFromFile(context: Context, file: File): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        }
        else
        {
            return Uri.fromFile(file)
        }
    }

    @JvmStatic
     fun createImageUri(context: Context) : Uri {

        val image = File(context.filesDir,"camera_photo.png")

        return FileProvider.getUriForFile(context.applicationContext,
            "com.policyboss.demoandroidapp.fileprovider",
            image
        )

    }




    fun openSetting(context: Context){
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    open fun createImageFile(name: String,context: Context ): File? {
        // Create an image file name
        val temp: File
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir = getAppSpecificAlbumStorageDir(
            context.applicationContext,
            Environment.DIRECTORY_PICTURES,
            "PolicyBossProElite"
        )
        try {
            temp = File.createTempFile(
                name + timeStamp,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )
            Log.d("IMAGE_PATH", "File Name" + temp.name + "File Path" + temp.absolutePath)
            //  String  currentPhotoPath = temp.getAbsolutePath();
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return temp
    }

    open fun getAppSpecificAlbumStorageDir(
        context: Context,
        albumName: String?,
        subAlbumName: String?
    ): File {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(context.getExternalFilesDir(albumName), subAlbumName)
        if (file.mkdirs()) {
            Log.e("fssfsf", "Directory not created")
        }
        return file
    }

    // URI TO Bitmap
     fun getBitmapFromContentResolver(selectedFileUri: Uri?, context: Context): Bitmap? {
        return try {
            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
                selectedFileUri!!, "r"
            )
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor!!.close()
            image
        } catch (e: IOException) {
            e.printStackTrace()

            null
        }
    }

     fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return encodeToString(byteArray, Base64.DEFAULT)
    }

//    private fun bitmapToBase64(bitmap: Bitmap): String? {
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }



    fun loadWebViewUrlInBrowser(context: Context, url: String?) {
        Log.d("URL", url!!)
        val browserIntent = Intent(Intent.ACTION_VIEW)
        if (Uri.parse(url) != null) {
            browserIntent.data = Uri.parse(url)
        }
        context.startActivity(browserIntent)
    }



    fun calculateAge(dob: String, customDateFormat : String ): Int {
        // Define the date format for parsing the DOB
        val dateFormat = SimpleDateFormat(customDateFormat, Locale.US)

        try {
            // Parse the DOB string to a Date object
            val dobDate = dateFormat.parse(dob)

            // Get the current date
            val currentDate = Calendar.getInstance().time

            // Calculate the difference in milliseconds between the current date and DOB
            val diffInMillis = currentDate.time - dobDate!!.time

            // Convert the difference to years
            val age = (diffInMillis / (1000L * 60 * 60 * 24 * 365.25)).toInt()

            return age
        } catch (e: Exception) {
            e.printStackTrace()
            return -1 // Return -1 if there's an error in parsing the DOB
        }
    }


    /****************************************************************
    //Note : Download any Type of File and Images Using URL
    ****************************************************************/
    open fun downloadFileFromUri( context: Context, url: String,mimeType: String, filename: String?): Uri? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.applicationContext.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            return if (uri != null) {
                URL(url).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }

                }
                uri
            } else {
                null
            }

        } else {

            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )
            URL(url).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }

            return target.toUri()
        }
    }

   open fun saveBitmapToUri(context: Context, bitmap: Bitmap, filename: String?): Uri? {
        val mimeType = "image/png"

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 (Q) and above


            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.applicationContext.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let {
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val inputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())

                resolver.openOutputStream(uri).use { output ->
                    inputStream.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                }
            }

            uri
        } else {
            // For devices running Android versions below Q
            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                filename
            )

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val inputStream = ByteArrayInputStream(byteArrayOutputStream.toByteArray())

            FileOutputStream(target).use { output ->
                inputStream.copyTo(output)
            }

            target.toUri()
        }
    }

    fun getPathFromUri(context: Context, uri: Uri): String? {
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, projection, null, null, null)
            cursor?.let {
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)


                    return cursor.getString(columnIndex)
                }
                cursor.close()
            }
        } else if (uri.scheme == ContentResolver.SCHEME_FILE) {
            return uri.path
        }
        return null
    }
    fun getMimeTypeFromUrl(url: String): String {
        return when {
            url.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
            url.endsWith(".jpg", ignoreCase = true) || url.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
            url.endsWith(".png", ignoreCase = true) -> "image/png"
            else -> "application/octet-stream" // Default MIME type for unknown file types
        }
    }


    fun shareData(context: Context,fileUri: Uri, mimeType: String ="image/*"){


        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*" // Set the MIME type based on the file type
        // shareIntent.type = "image/*"
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        shareIntent.putExtra(Intent.EXTRA_TEXT, "*****official Link*****")
// Set the URI as the content to share
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)

// Optionally set a subject for the shared content
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Content")

// Start the sharing activity

        context.startActivity(Intent.createChooser(shareIntent, "Share File"))

    }

    //not working
    open fun shareImageToGmail1(context: Context, imageUri: Uri) {
        // Try to get path first
        val imagePath = getPathFromUri(context, imageUri)

        if (imagePath != null) {
            // Share using image path directly
           // shareImageToGmail(context, imagePath) // Use the previous code
        }
        else
        {
            // Handle content URI directly
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_STREAM, imageUri)
         }
            try {
                context.startActivity(Intent.createChooser(intent, "Share Image via Email"))
            } catch (e: Exception) {
                Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareImageToGmail(context: Context, imageUri: Uri) {

        val imagePath = getPathFromUri(context, imageUri)
        val imageFile = File(imagePath)

        if (imageFile.exists()) {
            val uri: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    //"com.policyboss.demoandroidapp.fileprovider",
                    imageFile
                )
            } else {
                uri = Uri.fromFile(imageFile)
            }

            // Grant read permission to the content URI
            context.grantUriPermission(
                "com.google.android.gm",
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setDataAndType(uri, "image/*") // Set data and MIME type
                setPackage("com.google.android.gm")
                setType("message/rfc822")
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Customer Details")
                 putExtra(Intent.EXTRA_TEXT, "Please find custome Detail in attached file")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission
            }

            try {
                context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
            } catch (e: Exception) {
                Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Image file not found", Toast.LENGTH_SHORT).show()
        }
    }


    fun shareImage(context: Context, imageUri: Uri, subject: String, message: String) {
        val imagePath = getPathFromUri(context, imageUri)
        val imageFile = File(imagePath)

        if (imageFile.exists()) {
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    imageFile
                )
            } else {
                Uri.fromFile(imageFile)
            }

            // Grant read permission to the content URI
            context.grantUriPermission("com.android.email", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.grantUriPermission("com.google.android.gm", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.grantUriPermission("com.whatsapp", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.grantUriPermission("org.telegram.messenger", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.grantUriPermission("org.thoughtcrime.securesms", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION) //For Signal

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                setDataAndType(uri, "image/*") // Set data and MIME type
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission
            }

            try {
                context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
            } catch (e: Exception) {
                Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Image file not found", Toast.LENGTH_SHORT).show()
        }
    }



}