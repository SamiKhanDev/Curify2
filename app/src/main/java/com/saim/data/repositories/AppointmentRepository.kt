package com.saim.data.repositories

import com.saim.domain.entities.Appointment
import com.saim.domain.repositories.AppointmentRepositoryContract
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AppointmentRepository @javax.inject.Inject constructor() : AppointmentRepositoryContract {
    private fun userAppointmentsCollection(uid: String) =
        FirebaseFirestore.getInstance().collection("users").document(uid).collection("appointments")

    override suspend fun bookAppointment(uid: String, appointment: Appointment): Result<Boolean> {
        return try {
            val document = userAppointmentsCollection(uid).document()
            appointment.id = document.id
            appointment.uid = uid
            appointment.timestamp = System.currentTimeMillis()
            document.set(appointment).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelAppointment(uid: String, appointmentId: String): Result<Boolean> {
        return try {
            userAppointmentsCollection(uid).document(appointmentId)
                .update("status", "cancelled").await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAppointments(uid: String) = userAppointmentsCollection(uid)
        .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
        .snapshots()
        .map { it.toObjects(Appointment::class.java) }
}

