package com.policyboss.demoandroidapp.Utility

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import com.policyboss.demoandroidapp.R

class PopupView(private val context: Context) {

    private val dialog: Dialog

    init {

        dialog = Dialog(context,R.style.CustomDialogBottomtoTop)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.translucent_layout)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            //setGravity(Gravity.TOP)
        }

        val imageView: ImageView = dialog.findViewById(R.id.imgClose)

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
