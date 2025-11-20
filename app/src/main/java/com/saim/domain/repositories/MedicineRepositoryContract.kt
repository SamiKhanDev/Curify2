package com.saim.domain.repositories

import com.saim.domain.entities.Drugs
import kotlinx.coroutines.flow.Flow

interface MedicineRepositoryContract {
    suspend fun savemedicine(medicine: Drugs): Result<Boolean>
    suspend fun deletemedicine(medId: String): Result<Boolean>
    suspend fun updatemedicine(med: Drugs): Result<Boolean>
    fun getmedicine(): Flow<List<Drugs>>
}

