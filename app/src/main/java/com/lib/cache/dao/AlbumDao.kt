package com.lib.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lib.cache.model.CacheAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albums: List<CacheAlbum>)

    @Query("SELECT * from albums ORDER BY title")
    fun getAlbums(): Flow<List<CacheAlbum>>
}
