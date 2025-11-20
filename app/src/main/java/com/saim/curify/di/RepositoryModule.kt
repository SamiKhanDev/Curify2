package com.saim.curify.di

import com.saim.data.repositories.MedicineRepository
import com.saim.data.repositories.MyCartRepository
import com.saim.data.repositories.PrescriptionRepository
import com.saim.domain.repositories.MedicineRepositoryContract
import com.saim.domain.repositories.MyCartRepositoryContract
import com.saim.domain.repositories.PrescriptionRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMedicineRepository(impl: MedicineRepository): MedicineRepositoryContract

    @Binds
    @Singleton
    abstract fun bindMyCartRepository(impl: MyCartRepository): MyCartRepositoryContract

    @Binds
    @Singleton
    abstract fun bindPrescriptionRepository(impl: PrescriptionRepository): PrescriptionRepositoryContract
}

