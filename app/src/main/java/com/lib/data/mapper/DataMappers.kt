package com.lib.data.mapper

import com.lib.cache.model.CacheAlbum
import com.lib.data.model.Album
import com.lib.remote.models.RemoteAlbum

fun List<RemoteAlbum>.mapRemoteAlbumsToCache() = map { remoteAlbum ->
    CacheAlbum(
        id = remoteAlbum.id,
        userId = remoteAlbum.userId,
        title = remoteAlbum.title
    )
}

fun List<CacheAlbum>.mapCacheAlbumsToData() = map { cacheAlbum ->
    Album(
        id = cacheAlbum.id,
        userId = cacheAlbum.userId,
        title = cacheAlbum.title
    )
}
