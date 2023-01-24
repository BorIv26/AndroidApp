package com.example.nomenclature.di

import com.example.nomenclature.data.repository.RepositoryImpl
import com.example.nomenclature.domain.repository.Repository
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
    abstract fun bindRepository(
        userRepositoryImpl: RepositoryImpl
    ): Repository
}