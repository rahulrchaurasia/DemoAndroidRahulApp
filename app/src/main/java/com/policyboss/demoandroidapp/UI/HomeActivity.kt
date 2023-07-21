package com.policyboss.demoandroidapp.UI

import android.Manifest
import android.content.Intent
import android.net.wifi.hotspot2.pps.Credential
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.credentials.Credential.EXTRA_KEY
import com.policyboss.demoandroidapp.Constant
import com.policyboss.demoandroidapp.DesignPattern.DesignPatternDemoActivity
import com.policyboss.demoandroidapp.FileUpload.FileUploadActivity
import com.policyboss.demoandroidapp.FlowDemo.FlowDemoActivity
import com.policyboss.demoandroidapp.HiltDemo.HiltDemoActivity
import com.policyboss.demoandroidapp.TAG
import com.policyboss.demoandroidapp.UI.NavigationComponent.NavigationDemoMainActivity
import com.policyboss.demoandroidapp.UI.TextRecognizer.TextRecognizerActivity
import com.policyboss.demoandroidapp.UI.TextRecognizerDemo.CameraTextDetectActivity
import com.policyboss.demoandroidapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() ,View.OnClickListener {

    var CAMERA_PERMISSION_REQUEST_CODE = 101
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
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
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
    }

    fun setOnClickListener(){

        binding.btnHilt.setOnClickListener(this)
        binding.btnTextRecog.setOnClickListener(this)
        binding.btnNavComp.setOnClickListener(this)
        binding.btnDesignPattern.setOnClickListener(this)
        binding.btnFlow.setOnClickListener(this)
        binding.btnDeviceNo.setOnClickListener(this)
        binding.btnFileUpload.setOnClickListener(this)

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

            binding.btnNavComp.id -> {

                startActivity(Intent(this, NavigationDemoMainActivity::class.java))

            }

            binding.btnHilt.id -> {

                startActivity(Intent(this, HiltDemoActivity::class.java))

            }

            binding.btnFlow.id -> {

                startActivity(Intent(this, FlowDemoActivity::class.java))

            }

            binding.btnDesignPattern.id -> {

                startActivity(Intent(this, DesignPatternDemoActivity::class.java))

            }
            binding.btnDeviceNo.id -> {

                startActivity(Intent(this, DesignPatternDemoActivity::class.java))

            }

            binding.btnTextRecog.id -> {

               startActivity(Intent(this, CameraTextDetectActivity::class.java))

            }

            binding.btnFileUpload.id -> {

                startActivity(Intent(this, FileUploadActivity::class.java))

            }
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