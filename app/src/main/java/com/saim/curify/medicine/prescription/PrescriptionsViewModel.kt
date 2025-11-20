package com.saim.curify.medicine.prescription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saim.domain.entities.PrescriptionData
import com.saim.domain.repositories.PrescriptionRepositoryContract
import com.saim.curify.medicine.prescription.local.PrescriptionDao
import com.saim.curify.medicine.prescription.local.PrescriptionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionsViewModel @Inject constructor(
    private val repo: PrescriptionRepositoryContract,
    private val localDao: PrescriptionDao
) : ViewModel() {

    private val remoteItems = MutableStateFlow<List<PrescriptionData>>(emptyList())
    private val localItems = MutableStateFlow<List<PrescriptionData>>(emptyList())
    private val _items = MutableStateFlow<List<PrescriptionData>>(emptyList())
    val items: StateFlow<List<PrescriptionData>> = _items.asStateFlow()

    fun start(uid: String) {
        // Local first
        viewModelScope.launch {
            localDao.observe(uid).collectLatest { ents ->
                localItems.value = ents.map { it.toDomain() }
            }
        }
        // Remote
        viewModelScope.launch {
            repo.observe(uid).collectLatest { list ->
                remoteItems.value = list
            }
        }
        // Merge
        viewModelScope.launch {
            combine(localItems, remoteItems) { local, remote ->
                // Show local pending first, then remote
                val merged = ArrayList<PrescriptionData>()
                merged.addAll(local.filter { it.status.startsWith("local_") })
                // Avoid duplicates by id
                val remoteIds = remote.map { it.id }.toSet()
                merged.addAll(remote.filter { it.id !in remoteIds })
                if (merged.isEmpty()) remote else merged
            }.collectLatest { _items.value = it }
        }
    }

    private fun PrescriptionEntity.toDomain(): PrescriptionData = PrescriptionData().also {
        it.id = "local:${this.localId}"
        it.uid = this.uid
        it.imageUrl = this.imageUri
        it.note = this.note
        it.status = this.status
        it.timestamp = this.timestamp
    }
}

