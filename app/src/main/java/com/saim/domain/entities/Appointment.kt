package com.saim.domain.entities

data class Appointment(
    var id: String = "",
    var uid: String = "",
    var doctorId: String = "",
    var doctorName: String = "",
    var doctorPhoto: String = "",
    var specialization: String = "",
    var hospital: String = "",
    var fee: String = "",
    var date: String = "", // Format: "YYYY-MM-DD"
    var timeSlot: String = "", // Format: "HH:mm"
    var status: String = "pending", // pending, confirmed, cancelled, completed
    var timestamp: Long = System.currentTimeMillis(),
    var notes: String = ""
)

