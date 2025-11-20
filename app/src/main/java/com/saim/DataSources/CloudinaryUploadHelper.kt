package com.saim.DataSources

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.saim.curify.R

class CloudinaryUploadHelper {

    companion object {
        private var isInitialized = false
        private var unsignedPreset: String = ""
        private var cloudName: String = ""

        fun initializeCloudinary(context: Context) {
            if (!isInitialized) {
                cloudName = context.getString(R.string.cloudinary_cloud_name)
                unsignedPreset = context.getString(R.string.cloudinary_unsigned_preset)

                if (cloudName.isBlank() || unsignedPreset.isBlank()) {
                    Log.e("Cloudinary", "Cloudinary not configured: set cloudinary_cloud_name & cloudinary_unsigned_preset in strings.xml")
                    return
                }

                val config = mapOf(
                    "cloud_name" to cloudName,
                    "secure" to true
                )

                try {
                    MediaManager.init(context, config)
                    isInitialized = true
                    Log.d("Cloudinary", "MediaManager initialized | cloud_name=$cloudName | preset=$unsignedPreset")
                } catch (e: Exception) {
                    Log.e("Cloudinary", "Initialization failed: ${e.message}")
                }
            } else {
                Log.d("Cloudinary", "MediaManager already initialized")
            }
        }
    }

    fun uploadFile(
        filePathOrUri: String,
        onComplete: (Boolean, String?) -> Unit
    ) {

        if (!isInitialized || unsignedPreset.isBlank()) {
            onComplete(false, "Cloudinary is not configured. Initialize first.")
            return
        }

        val request = if (filePathOrUri.startsWith("content://") || filePathOrUri.startsWith("file://")) {
            MediaManager.get().upload(Uri.parse(filePathOrUri))
        } else {
            MediaManager.get().upload(filePathOrUri)
        }

        Log.d("Cloudinary", "Starting upload source=${if (filePathOrUri.startsWith("content://")||filePathOrUri.startsWith("file://")) "URI" else "Path"}")

        request
            .unsigned(unsignedPreset)
            .option("resource_type", "image")
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {
                    Log.d("Cloudinary", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    val percent = (bytes.toDouble() / totalBytes * 100).toInt()
                    Log.d("Cloudinary", "Upload progress: $percent%")
                }

                override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>?) {
                    val fileUrl = resultData?.get("secure_url") as? String
                    if (!fileUrl.isNullOrBlank()) {
                        Log.d("Cloudinary", "Upload success: $fileUrl")
                        onComplete(true, fileUrl)
                    } else {
                        Log.e("Cloudinary", "secure_url missing in response: $resultData")
                        onComplete(false, null)
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.e("Cloudinary", "Upload failed: ${error?.description}")
                    onComplete(false, error?.description)
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    Log.e("Cloudinary", "Upload rescheduled: ${error?.description}")
                }
            })
            .dispatch()
    }
}
