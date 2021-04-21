package com.lib.di

import com.lib.cache.LibDatabase
import com.lib.cache.dao.AlbumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlbumDaoModule {

    @Provides
    fun provideAlbumDao(db: LibDatabase): AlbumDao {
        return db.albumsDao()
    }
}
