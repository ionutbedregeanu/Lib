package com.lib.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lib.cache.dao.AlbumDao
import com.lib.cache.model.CacheAlbum

@Database(entities = [CacheAlbum::class], version = 1, exportSchema = false)
abstract class LibDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumDao
}
