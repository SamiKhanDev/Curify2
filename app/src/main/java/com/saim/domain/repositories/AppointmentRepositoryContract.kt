package com.saim.domain.repositories

import com.saim.domain.entities.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepositoryContract {
    suspend fun bookAppointment(uid: String, appointment: Appointment): Result<Boolean>
    suspend fun cancelAppointment(uid: String, appointmentId: String): Result<Boolean>
    fun getAppointments(uid: String): Flow<List<Appointment>>
}

