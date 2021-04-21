package com.lib.di

import com.lib.cache.dao.AlbumDao
import com.lib.data.SharedPrefsHelper
import com.lib.data.repository.AlbumsRepository
import com.lib.remote.services.AlbumService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRepository(
        service: AlbumService,
        albumDao: AlbumDao,
        sharedPrefsHelper: SharedPrefsHelper
    ): AlbumsRepository = AlbumsRepository(service, albumDao, sharedPrefsHelper)
}
