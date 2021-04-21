package com.lib.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "albums")
data class CacheAlbum(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String
)
