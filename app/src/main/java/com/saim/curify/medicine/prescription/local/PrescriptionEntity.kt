package com.saim.curify.medicine.prescription.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prescriptions")
data class PrescriptionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val uid: String,
    val imageUri: String,
    val note: String,
    val status: String = "pending",
    val timestamp: Long = System.currentTimeMillis()
)

