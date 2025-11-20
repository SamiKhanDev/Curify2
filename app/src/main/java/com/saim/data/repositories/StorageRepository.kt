package com.saim.data.repositories

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.util.UUID
import com.saim.domain.repositories.StorageRepositoryContract

class StorageRepository @javax.inject.Inject constructor() : StorageRepositoryContract {

    override fun uploadFile(filePath: String, onComplete: (Boolean, String?) -> Unit) {
        try {
            val app = FirebaseApp.getInstance()
            val projectId = app.options.projectId
            val storageUrl = "gs://" + projectId + ".appspot.com"
            val storage = FirebaseStorage.getInstance(storageUrl)
            val storageRef = storage.reference
            Log.d("StorageUpload", "Using storageUrl=${storageUrl}")
            val filename = UUID.randomUUID().toString() + ".jpg"
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "anonymous"
            val pathRef = storageRef.child("users/$uid/prescriptions/$filename")
            val uri: Uri = if (filePath.startsWith("content://") || filePath.startsWith("file://")) {
                Uri.parse(filePath)
            } else {
                Uri.fromFile(File(filePath))
            }
            Log.d("StorageUpload", "Resolved uri scheme=${uri.scheme} path=${uri.path}")
            Log.d("StorageUpload", "Starting Firebase upload uri=$uri -> users/$uid/prescriptions/$filename")
            val ctx = FirebaseApp.getInstance().applicationContext
            val mime = try { ctx.contentResolver.getType(uri) } catch (t: Throwable) { null }
            val metadata = StorageMetadata.Builder().setContentType(mime ?: "image/jpeg").build()
            val inputStream = try { ctx.contentResolver.openInputStream(uri) } catch (t: Throwable) { null }
            if (inputStream == null) {
                Log.e("StorageUpload", "Failed to open input stream for uri=$uri")
                onComplete(false, "Cannot read selected image")
                return
            }
            val uploadTask = pathRef.putStream(inputStream, metadata)
            uploadTask
                .addOnProgressListener { taskSnapshot ->
                    val total = taskSnapshot.totalByteCount.toDouble().coerceAtLeast(1.0)
                    val progress = (taskSnapshot.bytesTransferred / total * 100).toInt()
                    Log.d("StorageUpload", "Progress: $progress%")
                }
                .addOnFailureListener { e ->
                    val code = if (e is StorageException) e.errorCode else null
                    val http = if (e is StorageException) e.httpResultCode else null
                    Log.e("StorageUpload", "Upload failed code=${code} http=${http} msg=${e.message}")
                    onComplete(false, e.message)
                }
                .addOnSuccessListener {
                    Log.d("StorageUpload", "Upload bytes=${uploadTask.snapshot.totalByteCount}")
                    pathRef.downloadUrl
                        .addOnSuccessListener { downloadUri ->
                            Log.d("StorageUpload", "Upload success. URL=$downloadUri")
                            onComplete(true, downloadUri.toString())
                        }
                        .addOnFailureListener { e ->
                            val code = if (e is StorageException) e.errorCode else null
                            Log.e("StorageUpload", "Failed to get URL code=${code} msg=${e.message}")
                            onComplete(false, e.message)
                        }
                }
        } catch (e: Exception) {
            Log.e("StorageUpload", "Exception: ${e.message}")
            onComplete(false, e.message)
        }
    }
}