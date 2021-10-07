package com.test.challenge.ui

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.test.challenge.R
import com.test.challenge.data.API
import com.test.challenge.data.PermissionDeniedException
import com.test.challenge.utils.getBitmap
import com.test.challenge.utils.show
import java.io.File


class MainActivity : AppCompatActivity() {

    private val imageApi by lazy {
        API.getInstance(API.Config.filePath, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle Button Click listener and fetch image from network api
        findViewById<Button>(R.id.button).setOnClickListener {
            fetchImage()
        }

        checkPermissionAndLoadImage()
    }

    private fun checkPermissionAndLoadImage(){
        checkStoragePermission({
            // Load Image either from storage or network api
            loadImage()
        }, {
            show(getString(R.string.storage_permission_denied))
        })
    }

    /**
     * function load image and set value to image view
     */
    private fun loadImage(path : String = API.Config.filePath){
        val file = File(path)
        if (file.exists()) {
            file.getBitmap().let { imageBitmap ->
                findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)
            }
        } else {
            show(getString(R.string.image_not_found))
        }
    }

    /**
     * function fetch image from network api
     */
    private fun fetchImage(){
        imageApi.fetchImage(API.Config.imageWidthHeightList.shuffled()[0], {
            loadImage(it)
        }, {
            if (it.exception is PermissionDeniedException){
                checkPermissionAndLoadImage()
            }
            show(it.message)
        })
    }

    /**
     * Show storage permission dialog
     */
    private fun checkStoragePermission(granted : () -> Unit, denied : () -> Unit){
        Dexter.withContext(this@MainActivity)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    granted.invoke()
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {
                    denied.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken?.continuePermissionRequest()
                }

            }).check()
    }
}