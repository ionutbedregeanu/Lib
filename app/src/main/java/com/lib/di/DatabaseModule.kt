package com.lib.di

import android.app.Application
import androidx.room.Room
import com.lib.cache.LibDatabase
import com.lib.cache.dao.AlbumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

private const val DATABASE_NAME = "lib.db"

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDb(app: Application): LibDatabase {
        return Room
            .databaseBuilder(app, LibDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}
