package com.policyboss.demoandroidapp.Utility

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import java.math.RoundingMode
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun Context.showSnackbar(view: View, msg: String?) {
    Snackbar.make(view, msg ?: "Something went wrong", Snackbar.LENGTH_SHORT).show()
}

/*
example 1 (with action button):
val button: Button = findViewById(R.id.myButton)
button.setOnClickListener {
    context.showSnackbar(it, "Button clicked!", "Undo", object : View.OnClickListener {
        override fun onClick(v: View) {
            // Handle undo action

        }
    })
}

example 2 (without action button):

val textView: TextView = findViewById(R.id.myTextView)
context.showSnackbar(textView, "This is a message without action.")

 */
fun Context.showSnackbar(
    view: View,
    msg: String? = null,
    actionText: String? = null,
    actionListener: View.OnClickListener? = null
) {
    val message = msg ?: "Something went wrong"
    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)

    if (actionText != null && actionListener != null) {
        snackbar.setAction(actionText, actionListener)
    }

    snackbar.show()
}



fun Context.hideKeyboard(view: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard(etOtp: EditText) {
    etOtp.requestFocus()
    val inputMethodManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(etOtp, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

}

fun Context.getLocalIpAddress(): String? {
    try {
        val en = NetworkInterface.getNetworkInterfaces()
        while (en.hasMoreElements()) {
            val enumIpAddress = en.nextElement().inetAddresses
            while (enumIpAddress.hasMoreElements()) {
                val inetAddress = enumIpAddress.nextElement()
                if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                    return inetAddress.hostAddress
                }
            }
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
    }
    return null
}

fun Context.getUniqueID(): String = UUID.randomUUID().toString()

fun Context.twoDecimalFormat(value: Double): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.DOWN
    return df.format(value)
}


fun String?.formatCardNumber(): String {
    if (this == null || this.length < 10) return this ?: ""

    val firstSix = this.take(6)
    val lastFour = this.takeLast(4)
    val middleStars = "*".repeat(this.length - 10)

    return "$firstSix$middleStars$lastFour"
}

fun Calendar.formatDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(this.time)
}
