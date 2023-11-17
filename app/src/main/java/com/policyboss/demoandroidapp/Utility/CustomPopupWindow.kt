package com.policyboss.demoandroidapp.Utility

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.policyboss.demoandroidapp.R

class CustomPopupWindow(private val context: Context) {


    private lateinit var popupWindow: PopupWindow

    fun show(anchorView: View, message: String) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customView = inflater.inflate(R.layout.custom_alert_dialog, null)

        // Set your custom message
        val messageTextView: TextView = customView.findViewById(R.id.messageTextView)
        messageTextView.text = message

        // Measure the custom view to determine its width and height
        val measureRect = Rect()
        customView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val width = customView.measuredWidth
        val height = customView.measuredHeight

        // Calculate the location to display the PopupWindow
        val displaySize = Point()
        anchorView.display.getSize(displaySize)

        val location = IntArray(2)
        anchorView.getLocationInWindow(location)

        val x = location[0] + anchorView.width / 2 - width / 2
        val y = location[1] + anchorView.height

        // Create and show the PopupWindow
        popupWindow = PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
       // popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_popup_background))
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y)
    }

    fun dismiss() {
        if (::popupWindow.isInitialized && popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }
}
