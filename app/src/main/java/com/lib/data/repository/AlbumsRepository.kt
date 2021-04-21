package com.lib.data.repository

import com.lib.cache.dao.AlbumDao
import com.lib.data.SharedPrefsHelper
import com.lib.data.mapper.mapCacheAlbumsToData
import com.lib.data.mapper.mapRemoteAlbumsToCache
import com.lib.remote.models.RemoteAlbum
import com.lib.remote.services.AlbumService
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class AlbumsRepository @Inject constructor(
    private val albumService: AlbumService,
    private val albumDao: AlbumDao,
    private val sharedPrefs: SharedPrefsHelper
) {

    @Throws(Throwable::class)
    suspend fun populateLocalStorage(isForcedRefresh: Boolean = false): Boolean {
        if (isForcedRefresh || sharedPrefs.getFirstRequestValue()) {
            val remoteResponse = albumService.getAlbums()
            if (remoteResponse.isSuccessful && remoteResponse.body() != null) {
                albumDao.insert((remoteResponse.body() as List<RemoteAlbum>).mapRemoteAlbumsToCache())
            }
            sharedPrefs.setFirstRequestValue(false)
            return true
        }
        return false
    }

    fun getAlbumsFromDatabase() = albumDao.getAlbums().map { value -> value.mapCacheAlbumsToData() }
}
