package com.policyboss.demoandroidapp.Utility

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.Image

class customImageUtils {

    companion object {

        fun cropImage(image: Bitmap, cropRect: Rect): Bitmap {
            return Bitmap.createBitmap(image, cropRect.left, cropRect.top, cropRect.width(), cropRect.height())
        }

//        fun cropImage(image: Image, cropRect: Rect): Bitmap {
//            BitmapFactory.decodeResource(Resources.getSystem(), image)
//            return Bitmap.createBitmap(image, cropRect.left, cropRect.top, cropRect.width(), cropRect.height())
//        }
    }
}