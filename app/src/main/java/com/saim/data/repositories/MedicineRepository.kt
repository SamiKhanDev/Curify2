package com.saim.data.repositories

import com.saim.domain.entities.Drugs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.saim.domain.repositories.MedicineRepositoryContract

class MedicineRepository @javax.inject.Inject constructor() : MedicineRepositoryContract {
    val MedicineCollection = FirebaseFirestore.getInstance().collection("MedicineData")


    override suspend fun savemedicine(medicine: Drugs): Result<Boolean> {
        try {
            val document = MedicineCollection.document()
            medicine.id = document.id
            document.set(medicine).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)

        }
    }
    override suspend fun deletemedicine(medId: String): Result<Boolean> {
        return try {
            MedicineCollection.document(medId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun updatemedicine(med: Drugs): Result<Boolean> {
        try {
            val document = MedicineCollection.document(med.id)
            document.set(med).await()
            return Result.success(true)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun getmedicine() = MedicineCollection
        .snapshots()
        .map {it.toObjects(Drugs::class.java) }
}
