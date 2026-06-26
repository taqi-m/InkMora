package com.taqi.inkmora.di

import android.content.Context
import com.taqi.inkmora.data.local.ThemeSettingsStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideThemeSettingsStore(@ApplicationContext context: Context): ThemeSettingsStore {
        return ThemeSettingsStore(context)
    }
}
