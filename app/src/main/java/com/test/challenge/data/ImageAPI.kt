package com.test.challenge.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.test.challenge.R
import com.test.challenge.utils.writeBitmap
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File
import java.io.FileNotFoundException
import java.net.UnknownHostException
import java.util.*


/**
 * Image Api Endpoint Declaration
 */
interface ImageApiEndpoint {

    @GET(API.Path.resolution)
    fun fetchImage(
        @Path(API.Field.resolution, encoded = true) resolution: String?
    ): Call<ResponseBody>
}

/**
 * Function will return UserApi service using retrofit
 * @param : retrofit instance
 * @return : User Api service interface
 */
private fun provideImageApi(): ImageApiEndpoint {
    return Retrofit.Builder()
        .getBuild(API.Config.baseUrl)
        .create(ImageApiEndpoint::class.java)
}


/**
 * Class implement image api network call and handle response
 */
class ImageAPI(override val filePath: String, override val context: Context) : API{

    override fun fetchImage(resolution: String, success: (String) -> Unit, failure: (Error) -> Unit) {
        provideImageApi().fetchImage(resolution).enqueue(object : Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (t is UnknownHostException){
                    failure.invoke(Error(UnknownHostException(),
                        message = context.getString(R.string.no_connection)))
                } else
                    failure.invoke(Error(message = t.message.toString()))
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.byteStream()
                    val bmp = BitmapFactory.decodeStream(res)

                    bmp.let{
                        File(filePath).writeBitmap(bmp, Bitmap.CompressFormat.PNG, 85)
                        success.invoke(filePath)
                    }
                } catch (e: NullPointerException){
                    failure.invoke(Error(NullPointerException(), context.getString(R.string.something_went_wrong)))
                } catch (e: FileNotFoundException){
                    if (e.localizedMessage.toString().lowercase(Locale.getDefault())
                            .contains("permission denied")){
                        failure.invoke(Error(PermissionDeniedException(), context.getString(R.string.storage_permission_denied)))
                    } else {
                        failure.invoke(Error(e, context.getString(R.string.file_not_exists)))
                    }
                } catch (e: Exception){
                    failure.invoke(Error(message = e.message.toString()))
                }

            }
        })
    }
}

/**
 * Error class to maintain error message
 */
class Error(val exception: Exception = Exception(), val message: String)

/**
 * Add Custom Exception for permission denied and file not found
 */
class PermissionDeniedException : FileNotFoundException()
