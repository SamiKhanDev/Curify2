package com.saim.data.repositories

import com.saim.domain.entities.Doctor
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map

class DoctorRepository {
    private val collection = FirebaseFirestore.getInstance().collection("Doctors")

    fun getDoctors() = collection
        .snapshots()
        .map { it.toObjects(Doctor::class.java) }
}

