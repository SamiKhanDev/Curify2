package com.saim.curify.di

import com.saim.data.repositories.StorageRepository
import com.saim.domain.repositories.StorageRepositoryContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {
    @Binds
    @Singleton
    abstract fun bindStorageRepository(impl: StorageRepository): StorageRepositoryContract
}

