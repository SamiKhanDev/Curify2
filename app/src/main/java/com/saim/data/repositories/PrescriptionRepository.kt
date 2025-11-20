package com.saim.data.repositories

import com.saim.domain.entities.PrescriptionData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.saim.domain.repositories.PrescriptionRepositoryContract
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PrescriptionRepository @javax.inject.Inject constructor() : PrescriptionRepositoryContract {
    private fun userPrescriptions(uid: String) =
        FirebaseFirestore.getInstance().collection("users").document(uid).collection("prescriptions")

    override suspend fun save(uid: String, p: PrescriptionData): Result<Boolean> {
        return try {
            val doc = userPrescriptions(uid).document()
            p.id = doc.id
            p.uid = uid
            p.timestamp = System.currentTimeMillis()
            doc.set(p).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun observe(uid: String): Flow<List<PrescriptionData>> = callbackFlow {
        var registration: ListenerRegistration? = null
        try {
            registration = userPrescriptions(uid)
                .addSnapshotListener { snapshot, _ ->
                    val list = snapshot?.toObjects(PrescriptionData::class.java) ?: emptyList()
                    trySend(list.sortedByDescending { it.timestamp })
                }
        } catch (e: Exception) {
            trySend(emptyList())
        }
        awaitClose { registration?.remove() }
    }
}

