package com.saim.curify.appointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saim.domain.entities.Appointment
import com.saim.domain.repositories.AppointmentRepositoryContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val repository: AppointmentRepositoryContract
) : ViewModel() {

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isBookingSuccess = MutableStateFlow<Boolean?>(null)
    val isBookingSuccess: StateFlow<Boolean?> = _isBookingSuccess.asStateFlow()

    fun loadAppointments(uid: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.getAppointments(uid).collect { list ->
                    _appointments.value = list
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _isLoading.value = false
            }
        }
    }

    fun bookAppointment(uid: String, appointment: Appointment) {
        viewModelScope.launch {
            _isLoading.value = true
            _isBookingSuccess.value = null
            val result = repository.bookAppointment(uid, appointment)
            _isLoading.value = false
            if (result.isSuccess) {
                _isBookingSuccess.value = true
            } else {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to book appointment"
                _isBookingSuccess.value = false
            }
        }
    }

    fun cancelAppointment(uid: String, appointmentId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.cancelAppointment(uid, appointmentId)
            _isLoading.value = false
            if (result.isFailure) {
                _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to cancel appointment"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearBookingSuccess() {
        _isBookingSuccess.value = null
    }
}

