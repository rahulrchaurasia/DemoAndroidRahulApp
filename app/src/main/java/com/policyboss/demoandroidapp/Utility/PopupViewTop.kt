package com.policyboss.demoandroidapp.Utility

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import com.policyboss.demoandroidapp.R

class PopupViewTop(private val context: Context) {

    private val dialog: Dialog

    init {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_view)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.CYAN))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.TOP)
        }

        val imageView: ImageView = dialog.findViewById(R.id.imgClose)
        val closeButton: Button = dialog.findViewById(R.id.button1)

        closeButton.setOnClickListener {
            dismiss()
        }
        imageView.setOnClickListener {
            dismiss()
        }
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
