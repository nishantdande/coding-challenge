package com.test.challenge.data

import android.content.Context
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory

/**
 * Interface used to access retrofit api
 */
interface API {
    val filePath : String
    val context: Context

    fun fetchImage(resolution : String,
                   success: (String) -> Unit,
                   failure: (Error) -> Unit)

    companion object{
        fun getInstance(filePath: String, context: Context) : ImageAPI{
            return ImageAPI(filePath, context)
        }
    }

    object Config {
        const val baseUrl = "https://placekitten.com/"

        val imageWidthHeightList = listOf("200/300", "200/287",
            "200/140", "200/139", "200/286", "96/140",
            "96/139", "200/138")

        val filePath =
            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).absolutePath + "/image.jpg";
    }

    object Path {
        const val resolution = "/{resolution}"
    }

    object Field{
        const val resolution = "resolution"
    }
}

