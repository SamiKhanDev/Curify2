package com.saim.curify.di

import android.content.Context
import com.saim.curify.medicine.prescription.local.AppDatabase
import com.saim.curify.medicine.prescription.local.PrescriptionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.get(context)

    @Provides
    @Singleton
    fun providePrescriptionDao(db: AppDatabase): PrescriptionDao = db.prescriptionDao()
}

