package com.saim.curify.medicine.prescription.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PrescriptionEntity)

    @Query("SELECT * FROM prescriptions WHERE uid = :uid ORDER BY timestamp DESC")
    fun observe(uid: String): Flow<List<PrescriptionEntity>>
}

