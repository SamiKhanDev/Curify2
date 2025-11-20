package com.saim.domain.repositories

import com.saim.domain.entities.PrescriptionData
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction for prescription persistence.
 */
interface PrescriptionRepositoryContract {
    suspend fun save(uid: String, p: PrescriptionData): Result<Boolean>
    fun observe(uid: String): Flow<List<PrescriptionData>>
}

