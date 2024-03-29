package com.policyboss.demoandroidapp.UI

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import com.policyboss.demoandroidapp.BaseActivity


import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.CoroutineDemo.CoroutineDemo1Activity
import com.policyboss.demoandroidapp.DesignPattern.DesignPatternDemoActivity
import com.policyboss.demoandroidapp.FileUpload.FileUploadActivity
import com.policyboss.demoandroidapp.FlowDemo.FlowDemoActivity
import com.policyboss.demoandroidapp.HiltDemo.HiltDemoActivity
import com.policyboss.demoandroidapp.KotlinDemo.KotlinDemoActivity
import com.policyboss.demoandroidapp.ShareData.ShareActivityMain
import com.policyboss.demoandroidapp.ShareViewModel.ShareViewModeDemoActivity
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.UI.AutoCompleteDemo2.AutoCompDemo2Activity
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavigationDemoMainActivity
import com.policyboss.demoandroidapp.Utility.Utility
import com.policyboss.demoandroidapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date

class HomeActivity : BaseActivity() ,View.OnClickListener {

    var CAMERA_STORAGE_PERMISSION_REQUEST_CODE = 101

    lateinit var binding: ActivityHomeBinding

    val numbers = flowOf(1, 2, 3, 4, 5)

    companion object {
        var CREDENTIAL_PICKER_REQUEST = 1
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE),
            CAMERA_STORAGE_PERMISSION_REQUEST_CODE
        )
        setOnClickListener()

        val fruit = FruitModel(
            id = 1,
            title = FruitTitle.APPLE,
            image = "apple_image.jpg",
            price = "$1.99",
            color = "red"
        )

        val titleValue = fruit.title.name   // This will give you "APPLE"

        val color = CardType.GOLD.color
        Log.d(TAG, titleValue + " "+color)


        val currency = Currency.EUR
        Log.d(TAG,"Currency Name: ${currency.name}")
        Log.d(TAG,"Currency Code: ${currency.code}")
        Log.d(TAG,"Currency Symbol: ${currency.symbol}")
//        lifecycleScope.launch {
//
//            flowDemo()
//        }

        onBackPressedDispatcher.addCallback(this,object  : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                exitOnBackPressed()
            }

        })
    }

    fun setOnClickListener(){


        binding.btnDashBoard.setOnClickListener(this)
        binding.btnAPI.setOnClickListener(this)
        binding.btnAutoComplete.setOnClickListener(this)
        binding.btnAutoComplete.setOnClickListener(this)
        binding.btnHilt.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
        binding.btnGetPhotoFromContact.setOnClickListener(this)
        binding.btnNavComp.setOnClickListener(this)
        binding.btnDesignPattern.setOnClickListener(this)
        binding.btnFlow.setOnClickListener(this)
        binding.btnDeviceNo.setOnClickListener(this)
        binding.btnFileUpload.setOnClickListener(this)
        binding.btnKotlinDemo.setOnClickListener(this)
        binding.btnCoroutine.setOnClickListener(this)
        binding.btnShareViewModel.setOnClickListener(this)
    }


    fun exitOnBackPressed(){

       showAlert("Exit","Do you want to exit!!") { type  : String, dialog : DialogInterface ->

            when(type){
                "Y" -> {
                    // toast("Logout Successfully...!!")
                    this@HomeActivity.finish()
                }
                "N" -> {
                    dialog.dismiss()
                    toast("Cancel Exit")
                }

            }



        }

    }
    suspend fun flowDemo(){

//        numbers
//            .onEach { Log.d(Constant.TAG,"Processing number: $it") }
//            .map { it * it }
//            .onEach { Log.d(Constant.TAG,"Result: $it") }
//            .collect({
//
//                Log.d(Constant.TAG,"Collect:  ${it}" )
//            })



        numbers
            .map { it * it }

            .collect({

                Log.d(Constant.TAG,"Collect:  ${it}" )
            })

    }


    /*
    private fun phoneSelection() {
        // To retrieve the Phone Number hints, first, configure
        // the hint selector dialog by creating a HintRequest object.
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val options = CredentialsOptions.Builder()
            .forceEnableSaveDialog()
            .build()

        // Then, pass the HintRequest object to
        // credentialsClient.getHintPickerIntent()
        // to get an intent to prompt the user to
        // choose a phone number.
        val credentialsClient = Credentials.getClient(applicationContext, options)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(
                intent.intentSender,
                CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, Bundle()
            )
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

     */
    override fun onClick(view: View?) {


        when(view?.id){


            binding.btnAPI.id -> {

               // startActivity(Intent(this, Log::class.java))
               // displayLoadingWithText(binding.root, "Loading.." )
                downloadPDF(binding.root,this@HomeActivity)

              //  shareText(this@HomeActivity)

            }
            binding.btnShare.id ->{

                startActivity(Intent(this, ShareActivityMain::class.java))

            }
            binding.btnDashBoard.id -> {

                startActivity(Intent
                    (this, DashboardActivity::class.java)
                    .putExtra("csc","scw"))
            }
            binding.btnAutoComplete.id -> {

                startActivity(Intent(this, AutoCompDemo2Activity::class.java))

            }

            binding.btnNavComp.id -> {

                startActivity(Intent(this, NavigationDemoMainActivity::class.java))

            }
            binding.btnShareViewModel.id ->{

                startActivity(Intent(this, ShareViewModeDemoActivity::class.java))

            }

            binding.btnHilt.id -> {

                startActivity(Intent(this, HiltDemoActivity::class.java))

            }

            binding.btnFlow.id -> {

                startActivity(Intent(this, FlowDemoActivity::class.java))

            }
            binding.btnCoroutine.id -> {

                startActivity(Intent(this@HomeActivity, CoroutineDemo1Activity::class.java))


            }

            binding.btnDesignPattern.id -> {

                startActivity(Intent(this, DesignPatternDemoActivity::class.java))

            }
            binding.btnDeviceNo.id -> {

                startActivity(Intent(this, DesignPatternDemoActivity::class.java))

            }

            binding.btnGetPhotoFromContact.id -> {

              //  getPhotoFromDevice(this@HomeActivity)


            }

            binding.btnFileUpload.id -> {

                startActivity(Intent(this, FileUploadActivity::class.java))

            }
            binding.btnKotlinDemo.id -> {

                startActivity(Intent(this, KotlinDemoActivity::class.java))

            }

            ///

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
//
//            // get data from the dialog which is of type Credential
//            val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
//
//            // set the received data t the text view
//            credential?.apply {
//              //  tv1.text = credential.id
//            }
//        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
//            Toast.makeText(this, "No phone numbers found", Toast.LENGTH_LONG).show();
//        }
    }
}

enum class FruitTitle {
    APPLE, BANANA, CHERRY, CITRUS, GRAPES,
    GREEN_APPLE, ORANGE, PAPAYA, PEACH, PEACH1, PINEAPPLE, PUMPKIN, RASPBERRY, WATERMELON
}

data class FruitModel(
    var id: Int,
    var title: FruitTitle,
    var image: String,
    var price: String,
    var color: String
)

// Initializing Enum Constants
enum class CardType(val color: String) {
    SILVER("gray"),
    GOLD("yellow"),
    PLATINUM("black")
}

enum class Currency(val code: String, val symbol: String) {
    USD("USD", "$"),
    EUR("EUR", "€"),
    GBP("GBP", "£"),
    JPY("JPY", "¥"),
    INR("INR", "₹")
}


// Function to open a contact's display photo as Bitmap by display name



 fun downloadPDF(view : View,context: Context){

     //Note : We req Activity Context here not apllication COntext
     val context = context // Replace with your context

     val url = "https://origin-cdnh.policyboss.com/fmweb/policyboss-pro/uploads/sales_materialpb/814concern.jpg"
     val url1 = "https://origin-cdnh.policyboss.com/fmweb/policyboss-pro/uploads/salesmaterial/motor.png"
     val url2 = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf" // Replace with your file URL
   //  val mimeType = "application/pdf" // Replace with the appropriate MIME type
     val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
     val filename = "sampleDD"+ timeStamp // Replace with your desired filename

     (context as BaseActivity).displayLoadingWithText(view,"Loading")
   CoroutineScope(Dispatchers.IO).launch {

       val mimeType = Utility.getMimeTypeFromUrl(url2)

       val downloadedUritemp = async {
           Utility.downloadFileFromUri(context, url2, mimeType, filename)
       }

      val downloadedUri =  downloadedUritemp.await()
      withContext(Dispatchers.Main) {

          (context).hideLoading()
           if (downloadedUri != null) {


               Toast.makeText(context,"Fille Downloaded",Toast.LENGTH_LONG).show()
               Utility.shareData(context = context, fileUri =downloadedUri,mimeType = mimeType )
              // shareText(context)
           } else {
               // File download failed
               Toast.makeText(context,"Fille Downloaded Failed",Toast.LENGTH_LONG).show()
           }
       }

   }

 }




fun shareText(context: Context){


    val shareIntent = Intent(Intent.ACTION_SEND)

    shareIntent.type = "text/plain" // Set the MIME type for plain text
    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)


    shareIntent.putExtra(Intent.EXTRA_TEXT, "Text Message")
// Set the URI as the content to share
 //   shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)

// Optionally set a subject for the shared content
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Content")

// Start the sharing activity

    context.startActivity(Intent.createChooser(shareIntent, "Share File"))

}
 fun getPhotoFromDevice(context: Context){

     val contactIdsWithPhotos = getAllContactsWithPhotos(context )


     val myData  = mutableListOf<Bitmap>()
     for (contactId in contactIdsWithPhotos) {
         val inputStream = openPhoto(context, contactId.contactId)

         //val inputStream = openDisplayPhoto(context, contactId)
         val bitmap = BitmapFactory.decodeStream(inputStream)



         if (bitmap != null) {
             // Use the bitmap for contacts with photos as needed
             Log.d("CONTACT", " Count ${bitmap?.toString()?.length ?: 0}  ")
             myData.add(bitmap)
         } else {
             // Handle the case where the contact does not have a valid display photo
             Log.d("CONTACT", " No Data  ")
         }

     }
     Log.d("CONTACT", " Count ${myData.toString().length}  ")
//     for ((contactId, photoUri) in contactsList) {
//         if (photoUri != null) {
//             val bitmap = openDisplayPhotoAsStreamByContactId(context, contactId)
//             // Use the inputStream for each contact's display photo as needed
//
//
//
//             Log.d("CONTACT", " Count ${bitmap?.toString()?.length ?: 0}  ")
//         } else {
//             // Handle the case where the contact does not have a display photo
//         }
//     }

 }



// Function to retrieve all contact IDs with photos
fun getAllContactsWithPhotos1(context: Context): List<Long> {
    val contactIdsWithPhotos = mutableListOf<Long>()

    val projection = arrayOf(ContactsContract.Contacts._ID)
    val selection = "${ContactsContract.Contacts.PHOTO_URI} IS NOT NULL"
    val selectionArgs = null

    context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            contactIdsWithPhotos.add(contactId)
        }
    }

    return contactIdsWithPhotos
}


fun getAllContactsWithPhotos(context: Context): List<ContactInfo> {
    val contactsWithPhotos = mutableListOf<ContactInfo>()

    val projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    )
    val selection = "${ContactsContract.Contacts.PHOTO_URI} IS NOT NULL"
    val selectionArgs = null

    context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            val displayName = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

            val mobileNumbers = mutableListOf<String>()

            val phoneProjection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )
            val phoneSelection = "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"
            val phoneSelectionArgs = arrayOf(contactId.toString())

            context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                phoneProjection,
                phoneSelection,
                phoneSelectionArgs,
                null
            )?.use { phoneCursor ->
                while (phoneCursor.moveToNext()) {
                    val phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    mobileNumbers.add(phoneNumber)
                }
            }

            contactsWithPhotos.add(ContactInfo(contactId, displayName, mobileNumbers))
        }
    }

    return contactsWithPhotos
}

fun openPhoto(context: Context, contactId: Long): InputStream? {
    val contactUri: Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)

    // Try opening the display photo
    val displayPhotoUri: Uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
    try {
       // val contentResolver: ContentResolver = context.contentResolver
        val fd = context.contentResolver.openAssetFileDescriptor(displayPhotoUri, "r")
        if (fd != null) {
            return fd.createInputStream()
        }
    } catch (e: IOException) {
        // Failed to open display photo, proceed to retrieve thumbnail photo
    }

    // If display photo retrieval fails, try retrieving the thumbnail photo
    val photoUri: Uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
    val cursor = context.contentResolver.query(
        photoUri,
        arrayOf(ContactsContract.Contacts.Photo.PHOTO),
        null, null, null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val data = it.getBlob(0)
            if (data != null) {
                return ByteArrayInputStream(data)
            }
        }
    }

    return null
}
fun openDisplayPhoto(context: Context ,contactId: Long ): InputStream? {
    val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
    val displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
    return try {
        val fd: AssetFileDescriptor? =
            context.contentResolver.openAssetFileDescriptor(displayPhotoUri, "r")
        fd?.createInputStream()
    } catch (e: IOException) {
        null
    }
}

fun openPhotoThumbnail(context: Context, contactId: Long): InputStream? {
    val contactUri: Uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
    val photoUri: Uri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
    val cursor = context.contentResolver.query(
        photoUri,
        arrayOf(ContactsContract.Contacts.Photo.PHOTO),
        null, null, null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val data = it.getBlob(0)
            if (data != null) {
                return ByteArrayInputStream(data)
            }
        }
    }

    return null
}



fun openPhoto1(context: Context, contactId: Long): InputStream? {
    val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
    val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
    val projection = arrayOf(ContactsContract.Contacts.Photo.PHOTO)
   // val projection = arrayOf(ContactsContract.Contacts.Photo.DISPLAY_PHOTO)

    context.contentResolver.query(photoUri, projection, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
           // val data = cursor.getBlob(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.Photo.PHOTO))
            val data = cursor.getBlob(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.Photo.DISPLAY_PHOTO))
            if (data != null) {

               return ByteArrayInputStream(data)

            }
        }
    }

    return null
}

fun openDisplayPhotoAsBitmap(context: Context, contactId: Long): Bitmap? {


    val contactUri = ContactsContract.Contacts.getLookupUri(contactId, null)

    val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)

    try {
        val fd: AssetFileDescriptor? = context.contentResolver.openAssetFileDescriptor(photoUri, "r")
        fd?.use {
            val inputStream: InputStream = it.createInputStream()
            return BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null
}


fun getBitmapFromContentResolver(context: Context, selectedFileUri: Uri): Bitmap? {
    return try {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            context.contentResolver.openFileDescriptor(selectedFileUri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        image
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
fun openDisplayPhotoAsBitmapByDisplayName(context: Context, displayName: String): Bitmap? {
    val contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(displayName))
    val projection = arrayOf(ContactsContract.Contacts._ID)

    context.contentResolver.query(
        contactUri,
        projection,
        null,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            if (contactId != 0L) { // Make sure the contact ID is valid
                val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
                val displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)

                try {
                    val fd: AssetFileDescriptor? = context.contentResolver.openAssetFileDescriptor(displayPhotoUri, "r")
                    fd?.use {
                        val inputStream: InputStream = it.createInputStream()
                        return BitmapFactory.decodeStream(inputStream)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    return null
}


fun openDisplayPhotoAsStreamByContactId(context: Context, contactId: Long): Bitmap? {
    val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
    val displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)

    try {
//        val fd = context.contentResolver.openAssetFileDescriptor(displayPhotoUri, "r")
//        fd?.use {
//            return it.createInputStream()
//        }

        context.contentResolver.openInputStream(displayPhotoUri)?.use { inputStream ->
            return BitmapFactory.decodeStream(inputStream)
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null
}
fun getAllContactIdsAndPhotoUris(context: Context): List<Pair<Long, Uri?>> {
    val contactList = mutableListOf<Pair<Long, Uri?>>()

    val projection = arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.PHOTO_URI)
    val selection = null
    val selectionArgs = null

    context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            val photoUriString = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.PHOTO_URI))
            val photoUri = if (photoUriString != null) Uri.parse(photoUriString) else null
            contactList.add(Pair(contactId, photoUri))
        }
    }

    return contactList
}


data class ContactInfo(
    val contactId: Long,
    val displayName: String,
    val mobileNumbers: List<String>
)