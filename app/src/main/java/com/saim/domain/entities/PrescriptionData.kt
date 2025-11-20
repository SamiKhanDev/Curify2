package com.saim.domain.entities

class PrescriptionData {
    var id: String = ""
    var uid: String = ""
    var imageUrl: String = ""
    var note: String = ""
    var status: String = "pending" // pending, reviewed, approved, rejected
    var timestamp: Long = 0
}

