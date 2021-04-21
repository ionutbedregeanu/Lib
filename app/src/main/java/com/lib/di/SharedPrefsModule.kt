package com.lib.di

import android.app.Application
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefsModule {
    private const val ENCRYPTED_SHARED_PREFERENCES = "encrypted_sp"

    @Singleton
    @Provides
    internal fun provideSharedPreferences(app: Application) =
        EncryptedSharedPreferences.create(
            ENCRYPTED_SHARED_PREFERENCES,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            app,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
}
