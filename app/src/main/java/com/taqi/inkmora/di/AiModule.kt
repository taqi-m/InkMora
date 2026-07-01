package com.taqi.inkmora.di

import com.taqi.inkmora.data.repository.AiRepositoryImpl
import com.taqi.inkmora.domain.repository.AiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    @Provides
    @Singleton
    fun provideAiRepository(): AiRepository {
        return AiRepositoryImpl()
    }
}
