package com.test.challenge.utils

import android.R
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import java.io.File
import com.google.android.material.snackbar.Snackbar




/**
 * function write bitmap data to storage path
 */
fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
    outputStream().use { out ->
        bitmap.compress(format, quality, out)
        out.flush()
    }
}

/**
 * function return image bitmap from file path
 */
fun File.getBitmap() :  Bitmap {
    return BitmapFactory.decodeFile(this.absolutePath)
}

fun Activity.show(message: String){
    Snackbar.make(this.findViewById(R.id.content), message, Snackbar.LENGTH_LONG).apply {
        (this.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
            else
                gravity = Gravity.CENTER
        }

    }.show()
//    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}