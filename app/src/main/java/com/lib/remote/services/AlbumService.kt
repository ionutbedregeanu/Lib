package com.lib.remote.services

import com.lib.remote.models.RemoteAlbum
import retrofit2.Response
import retrofit2.http.GET

interface AlbumService {

    @GET("albums")
    suspend fun getAlbums(): Response<List<RemoteAlbum>>
}
