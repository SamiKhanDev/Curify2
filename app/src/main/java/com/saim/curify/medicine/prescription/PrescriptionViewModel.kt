package com.saim.curify.medicine.prescription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saim.curify.ServiceLocator
import com.saim.domain.entities.PrescriptionData
import com.saim.domain.repositories.PrescriptionRepositoryContract
import com.saim.domain.repositories.StorageRepositoryContract
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.saim.curify.medicine.prescription.local.PrescriptionDao
import com.saim.curify.medicine.prescription.local.PrescriptionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val storage: StorageRepositoryContract = ServiceLocator.storageRepository,
    private val repo: PrescriptionRepositoryContract = ServiceLocator.prescriptionRepository,
    private val localDao: PrescriptionDao
): ViewModel() {

    val isUploading = MutableStateFlow(false)
    val isSaved = MutableStateFlow<Boolean?>(null)
    val error = MutableStateFlow<String?>(null)

    fun uploadAndSave(uid: String, localPath: String, note: String) {
        isUploading.value = true
        android.util.Log.d("PrescriptionUpload", "Begin upload localPath=$localPath uid=${uid.isNotEmpty()}")
        storage.uploadFile(localPath) { success, result ->
            if (!success || result == null) {
                // Fallback: save locally via Room
                viewModelScope.launch {
                    try {
                        localDao.insert(
                            PrescriptionEntity(
                                uid = uid,
                                imageUri = localPath,
                                note = note,
                                status = "local_pending"
                            )
                        )
                        isUploading.value = false
                        android.util.Log.w("PrescriptionUpload", "Upload failed; saved locally")
                        isSaved.value = true
                    } catch (t: Throwable) {
                        isUploading.value = false
                        android.util.Log.e("PrescriptionUpload", "Local save error: ${t.message}")
                        error.value = result ?: "Upload failed"
                    }
                }
            } else {
                val p = PrescriptionData()
                p.imageUrl = result
                p.note = note
                viewModelScope.launch {
                    val saved = repo.save(uid, p)
                    isUploading.value = false
                    if (saved.isSuccess) {
                        android.util.Log.d("PrescriptionUpload", "Firestore save OK id=${p.id}")
                        isSaved.value = true
                    } else {
                        val msg = saved.exceptionOrNull()?.message
                        android.util.Log.e("PrescriptionUpload", "Firestore save error: $msg")
                        error.value = msg
                    }
                }
            }
        }
    }
}

