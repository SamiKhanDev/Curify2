package com.saim.curify

import com.saim.data.repositories.AuthRepository
import com.saim.data.repositories.PrescriptionRepository
import com.saim.data.repositories.StorageRepository
import com.saim.domain.repositories.AuthRepositoryContract
import com.saim.domain.repositories.PrescriptionRepositoryContract
import com.saim.domain.repositories.StorageRepositoryContract

/**
 * Minimal service locator to provide abstractions. Replace with a DI framework later (e.g., Hilt).
 */
object ServiceLocator {
    val authRepository: AuthRepositoryContract by lazy { AuthRepository() }
    val storageRepository: StorageRepositoryContract by lazy { StorageRepository() }
    val prescriptionRepository: PrescriptionRepositoryContract by lazy { PrescriptionRepository() }
}

